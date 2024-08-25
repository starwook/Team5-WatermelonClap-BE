package com.watermelon.server.orderResult.service;


import com.watermelon.server.order.dto.request.RequestAnswerDto;
import com.watermelon.server.order.dto.response.ResponseApplyTicketDto;
import com.watermelon.server.order.exception.NotDuringEventPeriodException;
import com.watermelon.server.order.exception.WrongOrderEventFormatException;

import com.watermelon.server.order.service.OrderResultSaveService;

import com.watermelon.server.token.ApplyTokenProvider;
import com.watermelon.server.token.JwtPayload;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.CannotCreateTransactionException;

import java.sql.SQLTransientConnectionException;

@Service
@RequiredArgsConstructor
public class OrderResultCommandService {

    private static final Logger log = LoggerFactory.getLogger(OrderResultCommandService.class);
    private final CurrentOrderEventManageService currentOrderEventManageService;
    private final ApplyTokenProvider applyTokenProvider;
    private final OrderResultSaveService orderResultSaveService;
    private final int toGetConnectionCount = 5;

    public ResponseApplyTicketDto createTokenAndMakeTicket(Long orderEventId) {
        String applyToken = applyTokenProvider.createTokenByOrderEventId(JwtPayload.from(String.valueOf(orderEventId)));
        for(int i=0;i<toGetConnectionCount;i++) {
            try{
                if(orderResultSaveService.isOrderApplyNotFullThenSaveConnectionOpen(applyToken)){ // 커넥션이 열리는 메소드
                    return ResponseApplyTicketDto.applySuccess(applyToken);
                }
                return ResponseApplyTicketDto.fullApply();
            }
            catch (CannotCreateTransactionException e){ //timeOut됐을시에
//                e.printStackTrace();
                log.info(i+"/1차 시도 실패");//한 번 씩 더 검사한다
                if(currentOrderEventManageService.isOrderApplyFull()){
                    return ResponseApplyTicketDto.fullApply();
                }
            }
        }
        return ResponseApplyTicketDto.fullApply();
    }



    public ResponseApplyTicketDto makeApplyTicket(RequestAnswerDto requestAnswerDto, Long orderEventId, Long quizId) throws NotDuringEventPeriodException, WrongOrderEventFormatException{
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
//         log.info("Locked OrderApplyCount record");
//        log.info("HikariCP Pool Status: ");
//        log.info("Active Connections: {}", dataSource.getHikariPoolMXBean().getActiveConnections());
//        log.info("Idle Connections: {}", dataSource.getHikariPoolMXBean().getIdleConnections());
//        log.info("Total Connections: {}", dataSource.getHikariPoolMXBean().getTotalConnections());
//        log.info("Threads Awaiting Connection: {}", dataSource.getHikariPoolMXBean().getThreadsAwaitingConnection());


}
