package com.watermelon.server.orderResult.service;

import com.watermelon.server.order.domain.OrderEvent;

import com.watermelon.server.order.domain.OrderEventStatus;
import com.watermelon.server.order.exception.NotDuringEventPeriodException;
import com.watermelon.server.order.exception.WrongOrderEventFormatException;
import com.watermelon.server.orderResult.repository.OrderApplyCountRepository;
import com.watermelon.server.orderResult.domain.OrderApplyCount;
import com.zaxxer.hikari.HikariDataSource;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Stack;


@Service
@RequiredArgsConstructor
public class CurrentOrderEventManageService {
    private static final Logger log = LoggerFactory.getLogger(CurrentOrderEventManageService.class);

    @Getter
    private OrderEvent currentOrderEvent;
    private final OrderApplyCountRepository orderApplyCountRepository;
    private Stack<String> customHandOffQueue = new Stack<>();


    public void addToCustomHandOffQueue(String applyTicket) {
        customHandOffQueue.push(applyTicket);
    }

    @Transactional(transactionManager = "orderResultTransactionManager")
    public boolean isOrderApplyNotFullThenPlusCount(){
        if(isOrderApplyFull()) {
            return false;
        }
        Optional<OrderApplyCount> orderApplyCountOptional = orderApplyCountRepository.findWithExclusiveLock();
        OrderApplyCount orderApplyCount = orderApplyCountOptional.get();
        if(currentOrderEvent.getWinnerCount()- orderApplyCount.getCount()>0){
             orderApplyCount.addCount();
             orderApplyCountRepository.save(orderApplyCount);
             return true;
        }
        this.currentOrderEvent.setOrderEventStatus(OrderEventStatus.CLOSED);
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
        if(currentOrderEvent != null && orderEventFromDB.getId().equals(currentOrderEvent.getId())){
            if(getCurrentApplyCount()<currentOrderEvent.getWinnerCount()){
                this.currentOrderEvent.setOrderEventStatus(OrderEventStatus.OPEN);
            }
            orderEventFromDB.setOrderEventStatus(currentOrderEvent.getOrderEventStatus());
            return;
        }
        currentOrderEvent = orderEventFromDB;
        clearOrderApplyCount();
    }


    @Transactional(transactionManager = "orderResultTransactionManager")
    public int getCurrentApplyCount() {
        if(orderApplyCountRepository.findAll().isEmpty()) orderApplyCountRepository.save(OrderApplyCount.builder().build());
        return orderApplyCountRepository.findCurrent().get().getCount();
    }


    @Transactional(transactionManager = "orderResultTransactionManager")
    public void clearOrderApplyCount() {
        orderApplyCountRepository.findCurrent().get().clearCount();
    }

    public boolean checkPrevious(String submitAnswer){
        if(currentOrderEvent.getQuiz().isCorrect(submitAnswer)) return true;
        return false;
    }
    public boolean isTimeInEvent(LocalDateTime now){
        if(now.isAfter(currentOrderEvent.getStartDate()) &&now.isBefore(currentOrderEvent.getEndDate())) return true;
        return false;
    }
    public boolean isEventAndQuizIdWrong( Long eventId,Long quizId) {
        if(currentOrderEvent !=null && currentOrderEvent.getId().equals(eventId) && currentOrderEvent.getQuiz().getId().equals(quizId)) return true;
        return false;
    }
    public void checkingInfoErrors( Long eventId, Long quizId)
            throws WrongOrderEventFormatException, NotDuringEventPeriodException {
        if (!isEventAndQuizIdWrong(eventId, quizId)) throw new WrongOrderEventFormatException();
        if (!isTimeInEvent(LocalDateTime.now())) throw new NotDuringEventPeriodException();
    }
    public Long getCurrentOrderEventId() {
        if (currentOrderEvent == null) return null;
        return this.currentOrderEvent.getId();
    }

    public boolean isOrderApplyFull() {
        if(currentOrderEvent.getOrderEventStatus().equals(OrderEventStatus.CLOSED))return true;
        return false;
    }
}
