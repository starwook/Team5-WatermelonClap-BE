package com.watermelon.server.order.result.service;


import com.watermelon.server.order.dto.request.RequestAnswerDto;
import com.watermelon.server.order.dto.response.ResponseApplyTicketDto;
import com.watermelon.server.order.exception.NotDuringEventPeriodException;
import com.watermelon.server.order.exception.WrongOrderEventFormatException;
import com.watermelon.server.order.repository.OrderResultRepository;
import com.watermelon.server.order.result.domain.OrderResult;
import com.watermelon.server.order.service. CurrentOrderEventManageService;

import com.watermelon.server.order.service.OrderResultSaveService;

import com.watermelon.server.token.ApplyTokenProvider;
import com.watermelon.server.token.JwtPayload;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderResultCommandService {

    private static final Logger log = LoggerFactory.getLogger(OrderResultCommandService.class);
    private final CurrentOrderEventManageService currentOrderEventManageService;
    private final ApplyTokenProvider applyTokenProvider;

    private final OrderResultSaveService orderResultSaveService;


    private final HikariDataSource dataSource;



    @Transactional
    public ResponseApplyTicketDto makeApplyTicket(RequestAnswerDto requestAnswerDto, Long orderEventId, Long quizId) throws NotDuringEventPeriodException, WrongOrderEventFormatException{
//        log.info("HikariCP Pool Status: ");
//        log.info("Active Connections: {}", dataSource.getHikariPoolMXBean().getActiveConnections());
//        log.info("Idle Connections: {}", dataSource.getHikariPoolMXBean().getIdleConnections());
//        log.info("Total Connections: {}", dataSource.getHikariPoolMXBean().getTotalConnections());
//        log.info("Threads Awaiting Connection: {}", dataSource.getHikariPoolMXBean().getThreadsAwaitingConnection());
        currentOrderEventManageService.checkingInfoErrors(orderEventId,quizId);
        // 퀴즈 틀릴 시에ApplyNotFullThenSave())
        if(currentOrderEventManageService.isOrderApplyFull()){ //
            return ResponseApplyTicketDto.fullApply();
        }
        if(!currentOrderEventManageService.checkPrevious(requestAnswerDto.getAnswer()))
        {
            return ResponseApplyTicketDto.wrongAnswer();
        }
        return createTokenAndMakeTicket(orderEventId);
    }

    public ResponseApplyTicketDto createTokenAndMakeTicket(Long orderEventId) {
        String applyToken = applyTokenProvider.createTokenByOrderEventId(JwtPayload.from(String.valueOf(orderEventId)));
        OrderResult orderResult = OrderResult.makeOrderEventApply(applyToken);
        if(saveOrderResultIfCan(orderResult)){
            orderResultSaveService.saveOrderResult(orderResult);
            return ResponseApplyTicketDto.applySuccess(applyToken);
        }
        return ResponseApplyTicketDto.fullApply();
    }

    public boolean saveOrderResultIfCan(OrderResult orderResult){
        if(currentOrderEventManageService.isOrderApplyNotFullThenPlusCount()){
            return true;
        }
        return false;
    }

    @Transactional
    public void saveOrderResult(OrderResult orderResult){
        orderResultRepository.save(orderResult);
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
