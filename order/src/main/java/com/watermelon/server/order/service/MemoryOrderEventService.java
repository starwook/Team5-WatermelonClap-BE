package com.watermelon.server.order.service;

import com.watermelon.server.order.domain.OrderEvent;

import com.watermelon.server.order.domain.OrderEventStatus;
import com.watermelon.server.order.exception.NotDuringEventPeriodException;
import com.watermelon.server.order.exception.WrongOrderEventFormatException;
import com.watermelon.server.order.service.orderApplyCount.OrderEventWinningCountService;
import com.watermelon.server.order.repository.OrderApplyCountRepository;
import com.watermelon.server.order.domain.OrderWinningCount;
import com.watermelon.server.order.service.orderApply.BlockingQueueTokenForDbAccessProviderService;
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
public class MemoryOrderEventService {
    private static final Logger log = LoggerFactory.getLogger(MemoryOrderEventService.class);

    @Getter
    private volatile OrderEvent orderEventFromServerMemory;
    private final OrderApplyCountRepository orderApplyCountRepository;
    private final BlockingQueueTokenForDbAccessProviderService blockingQueueTokenForDbAccessProviderService;
    private final OrderEventWinningCountService orderEventWinningCountService;
    @Getter
    @Setter
    private List<OrderWinningCount> orderWinningCountsFromServerMemory = new ArrayList<>();

    public boolean isOrderApplyNotFullThenPlusCount(){
        if(isOrderApplyFull()) {
            return false;
        }
        OrderWinningCount orderWinningCountFromServerMemory = orderEventFromServerMemory.getOrderWinningCount();
        int winnerCount = orderEventFromServerMemory.getWinnerCount();
        // 이 곳에서 락을 걸고 가져온다.
        if(orderEventWinningCountService.isOrderApplyCountAddable(orderWinningCountFromServerMemory.getId(),winnerCount)){
            orderWinningCountFromServerMemory.addCountOnce();
            return true;
        }
        orderEventFromServerMemory.setOrderEventStatus(OrderEventStatus.CLOSED);
        return false;
    }

    @Transactional
    public void refreshOrderEventInProgress(OrderEvent orderEventFromDB){
        /**
         * 서버에 임시적으로 저장되어있는 OrderEvent와 DB에서 온 인자의 OrderEvent가 같다면
         * 실제 DB에 서버에 임시적으로 저장되어있는 OrderEvent의 상태를 덮어씌운다.
         *
         * 하지만 다르다면 서버에 저장되어있는 OrderEvent를 최신화 시켜주고
         * 당첨자 수 또한 초기화 시켜준다.
         */
        if(orderEventFromServerMemory != null && orderEventFromDB.getId().equals(orderEventFromServerMemory.getId())){
            orderEventFromDB.setOrderEventStatus(orderEventFromServerMemory.getOrderEventStatus());
            return;
        }
        orderEventFromServerMemory = orderEventFromDB;
        refreshApplyCount();
    }


    @Transactional(transactionManager = "orderApplyCountTransactionManager")
    public void refreshApplyCount() {
        clearOrderApplyCount();
        blockingQueueTokenForDbAccessProviderService.refreshQueue();
        blockingQueueTokenForDbAccessProviderService.addIndexToQueue(orderEventFromServerMemory.getWinnerCount(), orderWinningCountsFromServerMemory.size());
        orderEventFromServerMemory.setOrderEventStatus(OrderEventStatus.OPEN);
    }

    public void clearOrderApplyCount() {
        /**
         * 서버에 저장되고 있는 이벤트가 바뀔 때마다 실행되는 메소드
         * 1. 모든 ApplyCount 레코드들을 초기화 시켜주고
         * 2. 현재 요청이 들어온다면 접근해야하는 ApplyCount의 ID의 인덱스가 저장되어있는 변수를 초기화 해준다.
         */
        orderWinningCountsFromServerMemory = orderApplyCountRepository.findAll();
        orderWinningCountsFromServerMemory.forEach(OrderWinningCount::clearCount);
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
