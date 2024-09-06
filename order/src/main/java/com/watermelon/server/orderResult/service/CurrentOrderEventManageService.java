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

    @Getter
    private List<OrderApplyCount> orderApplyCountsFromServerMemory = new ArrayList<>();

    @Transactional(transactionManager = "orderResultTransactionManager")
    public boolean isOrderApplyNotFullThenPlusCount(){
        log.info(orderEventFromServerMemory.toString());
        if(isOrderApplyFull()) {
            return false;
        }
        /**
         * DB에 저장되어서 Lock을 걸고 가져오는 OrderApplyCount와
         * 서버에 저장되어있는 OrderApplyCount를 분리하여 관리
         */
        OrderApplyCount orderApplyCountFromServerMemory = null;
        OrderApplyCount orderApplyCountFromDB =null;
        for(int i = 0; i< orderApplyCountsFromServerMemory.size(); i++){
            /**
             * 서버에 저장되어있는 OrderApplyCount가 꽉 차지 않았을때만 락을 걸고 동일 ID를 가지고 있는 OrderApplyCount를 가져온다
             */
            if(orderApplyCountsFromServerMemory.get(i).isFull()) continue;
            orderApplyCountFromServerMemory = orderApplyCountsFromServerMemory.get(i);
            orderApplyCountFromDB =
                    orderApplyCountRepository.findWithIdExclusiveLock(orderApplyCountFromServerMemory.getId()).get();

            if(orderEventFromServerMemory.getWinnerCount()/ orderApplyCountsFromServerMemory.size()- orderApplyCountFromDB.getCount()>0){
                orderApplyCountFromDB.addCount();
                orderApplyCountRepository.save(orderApplyCountFromDB);
                return true;
            }
            /**
             * 이 부분에 오면 현재의 OrderApplyCount가 꽉 찼다는 의미이다.
             */
            orderApplyCountFromServerMemory.makeFull();
            orderApplyCountFromDB.makeFull();
        }
        /**
         * 모든 ApplyCount가 꽉 차지 않았다면 return false만 해주고, 모두 꽉 찼다면 현재 이벤트의 상태 flag를 바꾼다
         */
        for(OrderApplyCount eachOrderApplyCount : orderApplyCountsFromServerMemory){
            if(!eachOrderApplyCount.isFull()){
                return false;
            }
        }
        orderEventFromServerMemory.setOrderEventStatus(OrderEventStatus.CLOSED);
        return false;
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

            log.info(orderEventFromServerMemory.toString());
            if(!checkIfApplyCountFull()){
                orderEventFromServerMemory.setOrderEventStatus(OrderEventStatus.OPEN);
            }
            orderEventFromDB.setOrderEventStatus(orderEventFromServerMemory.getOrderEventStatus());
            return;
        }
        orderEventFromServerMemory = orderEventFromDB;
        clearOrderApplyCount();
    }

    @Transactional(transactionManager = "orderResultTransactionManager")
    public boolean checkIfApplyCountFull() {
        boolean isAllApplyCountFull = true;
        List<OrderApplyCount> orderApplyCountsFromDB = orderApplyCountRepository.findAll();
        for(OrderApplyCount eachOrderApplyCount : orderApplyCountsFromDB){
            if(eachOrderApplyCount.getCount()<orderEventFromServerMemory.getWinnerCount()/orderApplyCountsFromDB.size()){
                eachOrderApplyCount.makeNotFull();
                isAllApplyCountFull = false;
                orderApplyCountRepository.save(eachOrderApplyCount);
            }
        }
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
