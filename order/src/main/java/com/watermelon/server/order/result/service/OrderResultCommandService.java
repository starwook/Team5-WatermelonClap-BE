package com.watermelon.server.order.result.service;


import com.watermelon.server.order.dto.request.RequestAnswerDto;
import com.watermelon.server.order.dto.response.ResponseApplyTicketDto;
import com.watermelon.server.order.error.NotDuringEventPeriodException;
import com.watermelon.server.order.error.WrongOrderEventFormatException;
import com.watermelon.server.order.result.domain.OrderResult;
import com.watermelon.server.order.service.CurrentOrderEventManageService;
import com.watermelon.server.redis.annotation.RedisDistributedLock;
import com.watermelon.server.token.ApplyTokenProvider;
import com.watermelon.server.token.JwtPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderResultCommandService {

    private final CurrentOrderEventManageService currentOrderEventManageService;
    private final ApplyTokenProvider applyTokenProvider;

    @Transactional
    public ResponseApplyTicketDto makeApplyTicket(RequestAnswerDto requestAnswerDto, Long orderEventId, Long quizId) throws NotDuringEventPeriodException, WrongOrderEventFormatException {
        currentOrderEventManageService.checkingInfoErrors(orderEventId,quizId);
        // 퀴즈 틀릴 시에
        if(!currentOrderEventManageService.isAnswerCorrect(requestAnswerDto.getAnswer()))
        {
            return ResponseApplyTicketDto.wrongAnswer();
        }
        return createTokenAndMakeTicket(orderEventId);
    }

    @Transactional
    public ResponseApplyTicketDto createTokenAndMakeTicket(Long orderEventId){
        String applyToken = applyTokenProvider.createTokenByOrderEventId(JwtPayload.from(String.valueOf(orderEventId)));
        OrderResult orderResult = OrderResult.makeOrderEventApply(applyToken);
        if(saveOrderResultWithLock(orderResult)){
            return ResponseApplyTicketDto.applySuccess(applyToken);
        }
        return ResponseApplyTicketDto.fullApply();
    }


    @RedisDistributedLock(key = "orderResultLock")
    public boolean saveOrderResultWithLock(OrderResult orderResult){
        if(currentOrderEventManageService.isOrderApplyNotFullThenSave(orderResult)){
            return true;
        }
        return false;
    }
//    //저장 할시에 확실하게 돌려주어야함 - 하지만 돌려주지 못 할시에는 어떻게?( 로그인이 안 되어있음)
//
//    @Transactional
//    public boolean saveResponseResultWithOutLock(OrderResult orderResult){
//        if(orderResultQueryService.isOrderApplyNotFull()){
//            orderResultSet.add(orderResult);
//            return true;
//        }
//        return false;
//    }

}
