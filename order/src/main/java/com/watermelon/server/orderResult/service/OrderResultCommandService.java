package com.watermelon.server.orderResult.service;


import com.watermelon.server.order.dto.request.RequestAnswerDto;
import com.watermelon.server.order.dto.response.ResponseApplyTicketDto;
import com.watermelon.server.order.exception.NotDuringEventPeriodException;
import com.watermelon.server.order.exception.WrongOrderEventFormatException;

import com.watermelon.server.order.service.OrderResultSaveService;

import com.watermelon.server.token.ApplyTokenProvider;
import com.watermelon.server.token.JwtPayload;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderResultCommandService {

    private static final Logger log = LoggerFactory.getLogger(OrderResultCommandService.class);
    private final CurrentOrderEventManageService currentOrderEventManageService;
    private final ApplyTokenProvider applyTokenProvider;
    private final OrderResultSaveService orderResultSaveService;
    private final IndexLoadBalanceService indexLoadBalanceService;
    @Qualifier("orderResultDatasource") //timeOut이 다른 커넥션을 가져온다.
    @Getter
    private final HikariDataSource dataSource;

    /**
     * DB 접근이 가능한지 확인하기 이전에
     * 이벤트 ID, 퀴즈 정답, 꽉 찼는지 Flag 변수 등
     * 빠르게 검증 가능한 것들을 먼저 검증한다.
     */
    public ResponseApplyTicketDto makeApplyTicket(RequestAnswerDto requestAnswerDto, Long orderEventId, Long quizId) throws NotDuringEventPeriodException, WrongOrderEventFormatException{
        currentOrderEventManageService.checkingInfoErrors(orderEventId,quizId);
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
        try{
            /**
             * ApplyCountIndex는 당첨자 수만큼 배정된다. 그리고 ApplyCountIndex에는 접근해야하는 ApplyCount 레코드의 정보 또한 가지고 있다.
             * ApplyCountIndex를 할당받지 못 한다면 즉 이미 모든 값들이 할당되었다면, NullPointerException이 발생한다.
             */
            int applyCountIndex = indexLoadBalanceService.getIndex();
            /**
             * 에러가 발생하지 않고 할당이 성공했을 때만  토큰을 생성하고 DB커넥션을 여는 메소드를 실행한다.
             */
            String applyToken = applyTokenProvider.createTokenByOrderEventId(JwtPayload.from(String.valueOf(orderEventId)));
            if(orderResultSaveService.isOrderApplyNotFullThenSaveConnectionOpen(applyToken,applyCountIndex)){ // 커넥션이 열리는 메소드
                return ResponseApplyTicketDto.applySuccess(applyToken);
            }
            return ResponseApplyTicketDto.fullApply();
        }catch (NullPointerException noMoreIndexException){
            noMoreIndexException.printStackTrace();
            return ResponseApplyTicketDto.fullApply();
        }
    }

    public void refreshApplyCount(){
        currentOrderEventManageService.refreshApplyCount();
    }

//         log.info("Locked OrderApplyCount record");
//        log.info("HikariCP Pool Status: ");
//        log.info("Active Connections: {}", dataSource.getHikariPoolMXBean().getActiveConnections());
//        log.info("Idle Connections: {}", dataSource.getHikariPoolMXBean().getIdleConnections());
//        log.info("Total Connections: {}", dataSource.getHikariPoolMXBean().getTotalConnections());
//        log.info("Threads Awaiting Connection: {}", dataSource.getHikariPoolMXBean().getThreadsAwaitingConnection());


}
