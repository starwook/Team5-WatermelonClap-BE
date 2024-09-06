package com.watermelon.server.orderResult.service;

import com.watermelon.server.order.domain.OrderEvent;

import com.watermelon.server.order.domain.OrderEventStatus;
import com.watermelon.server.order.exception.NotDuringEventPeriodException;
import com.watermelon.server.order.exception.WrongOrderEventFormatException;
import com.watermelon.server.orderResult.repository.OrderApplyCountRepository;
import com.watermelon.server.orderResult.domain.OrderApplyCount;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class CurrentOrderEventManageService {
    private static final Logger log = LoggerFactory.getLogger(CurrentOrderEventManageService.class);

    @Getter
    private volatile OrderEvent orderEventFromServerMemory;
    private final OrderApplyCountRepository orderApplyCountRepository;

    private final IndexLoadBalanceService indexLoadBalanceService;

    @Getter
    private List<OrderApplyCount> orderApplyCountsFromServerMemory = new ArrayList<>();

    @Transactional(transactionManager = "orderResultTransactionManager")
    public boolean isOrderApplyNotFullThenPlusCount(int applyCountIndex){
        if(isOrderApplyFull()) {
            return false;
        }
        /**
         * DB에 저장되어서 Lock을 걸고 가져오는 OrderApplyCount와
         * 서버에 저장되어있는 OrderApplyCount를 분리하여 관리
         */
        try{
            OrderApplyCount orderApplyCountFromServerMemory = null;
            OrderApplyCount orderApplyCountFromDB =null;

            /**
             * 현재 접근해야하는 ApplyCount의 Index(PK아 님)를 배정받은 후에
             * 서버에 저장되어있는 ApplyCount 목록에서 해당 Index의 ApplyCount를 가져온다
             * 그 이후에 해당 ApplyCount의 ID로 DB에 비관적 락을 걸고 접근한다.
             */
            orderApplyCountFromServerMemory = orderApplyCountsFromServerMemory.get(applyCountIndex);
            if(orderApplyCountFromServerMemory.isFull()) return false;

            orderApplyCountFromDB =
                    orderApplyCountRepository.findWithIdExclusiveLock(orderApplyCountFromServerMemory.getId()).get();

            int eachMaxWinnerCount = orderEventFromServerMemory.getWinnerCount()/orderApplyCountsFromServerMemory.size();
            if(eachMaxWinnerCount > orderApplyCountFromDB.getCount()){
                orderApplyCountFromDB.addCount();
                orderApplyCountRepository.save(orderApplyCountFromDB);
                /**
                 * 만약 각 ApplyCount에 정해진 개수만큼 꽉 찼다면
                 * 해당 ApplyCount의 flag를 full로 만들어준다.
                 */
                if(eachMaxWinnerCount == orderApplyCountFromDB.getCount()){
                    orderApplyCountFromServerMemory.makeFull();
                    orderApplyCountFromDB.makeFull();
                    orderApplyCountRepository.save(orderApplyCountFromDB);
                }
                return true;
            }
            return false;
        }
        finally {
            /**
             * 모든 ApplyCount의 flag가 full이라면 현재 이벤트의 상태 flag를 바꾼다
             */
            boolean allFull = true;
            for(OrderApplyCount eachOrderApplyCount : orderApplyCountsFromServerMemory){
                if(!eachOrderApplyCount.isFull()){
                    allFull = false;
                }
            }
            if(allFull){orderEventFromServerMemory.setOrderEventStatus(OrderEventStatus.CLOSED);}
        }
    }

    @Transactional
    public void refreshOrderEventInProgress(OrderEvent orderEventFromDB){
        /**
         * 서버에 임시적으로 저장되어있는 OrderEvent와 DB에서 온 인자의 OrderEvent가 같다면
         * 실제 DB에서 온 OrderEvent에, 서버에 임시적으로 저장되어있는 OrderEvent의 상태를 덮어씌운다.
         *
         * 하지만 다르다면 서버에 저장되어있는 OrderEvent를 최신화 시켜주고
         * 당첨자 수 또한 초기화 시켜준다.
         */
        if(orderEventFromServerMemory != null && orderEventFromDB.getId().equals(orderEventFromServerMemory.getId())){
            if(!checkIfApplyCountFull()){
                orderEventFromServerMemory.setOrderEventStatus(OrderEventStatus.OPEN);
            }
            orderEventFromDB.setOrderEventStatus(orderEventFromServerMemory.getOrderEventStatus());
            return;
        }
        orderEventFromServerMemory = orderEventFromDB;
        clearOrderApplyCount();
        indexLoadBalanceService.addIndexToQueue(orderEventFromServerMemory.getWinnerCount(), orderApplyCountsFromServerMemory.size());
    }

    @Transactional(transactionManager = "orderResultTransactionManager")
    public boolean checkIfApplyCountFull() {
        boolean isAllApplyCountFull = true;
        int remainWinnerCount = 0;
        List<OrderApplyCount> orderApplyCountsFromDB = orderApplyCountRepository.findAll();
        for(OrderApplyCount eachOrderApplyCount : orderApplyCountsFromDB){
            int eachMaxWinnerCount = orderEventFromServerMemory.getWinnerCount()/orderApplyCountsFromDB.size();
            int eachRemainWinnerCount = eachOrderApplyCount.getCount();
            if(eachRemainWinnerCount<eachMaxWinnerCount){
                if(eachOrderApplyCount.isFull()){
                    remainWinnerCount += (eachMaxWinnerCount-eachRemainWinnerCount);
                    log.info("remain Winner Count = {}" ,remainWinnerCount);
                    eachOrderApplyCount.makeNotFull();
                    isAllApplyCountFull = false;
                    orderApplyCountRepository.save(eachOrderApplyCount);
                }
            }
        }
        indexLoadBalanceService.addIndexToQueue(remainWinnerCount, orderApplyCountsFromDB.size());
        orderApplyCountsFromServerMemory = orderApplyCountsFromDB;
        return isAllApplyCountFull;
    }
    @Transactional(transactionManager = "orderResultTransactionManager")
    public void clearOrderApplyCount() {
        /**
         * 서버에 저장되고 있는 이벤트가 바뀔 때마다 실행되는 메소드
         * 1. 모든 ApplyCount 레코드들을 초기화 시켜주고
         * 2. 현재 요청이 들어온다면 접근해야하는 ApplyCount의 ID의 인덱스가 저장되어있는 변수를 초기화 해준다.
         */
        orderApplyCountsFromServerMemory = orderApplyCountRepository.findAll();
        orderApplyCountsFromServerMemory.forEach(OrderApplyCount::clearCount);
    }

    public boolean checkPrevious(String submitAnswer){
        if(orderEventFromServerMemory.getQuiz().isCorrect(submitAnswer)) return true;
        return false;
    }
    public boolean isTimeInEvent(LocalDateTime now){
        if(now.isAfter(orderEventFromServerMemory.getStartDate()) &&now.isBefore(orderEventFromServerMemory.getEndDate())) return true;
        return false;
    }
    public boolean isEventAndQuizIdWrong( Long eventId,Long quizId) {
        if(orderEventFromServerMemory !=null && orderEventFromServerMemory.getId().equals(eventId) && orderEventFromServerMemory.getQuiz().getId().equals(quizId)) return true;
        return false;
    }
    public void checkingInfoErrors( Long eventId, Long quizId)
            throws WrongOrderEventFormatException, NotDuringEventPeriodException {
        if (!isEventAndQuizIdWrong(eventId, quizId)) throw new WrongOrderEventFormatException();
        if (!isTimeInEvent(LocalDateTime.now())) throw new NotDuringEventPeriodException();
    }
    public Long getCurrentOrderEventId() {
        if (orderEventFromServerMemory == null) return null;
        return this.orderEventFromServerMemory.getId();
    }

    public boolean isOrderApplyFull() {
        if(orderEventFromServerMemory.getOrderEventStatus().equals(OrderEventStatus.CLOSED)){
            return true;
        }
        return false;
    }
}
