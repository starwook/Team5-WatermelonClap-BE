package com.watermelon.server.event.order.service;

import com.watermelon.server.event.order.domain.OrderEvent;
import com.watermelon.server.event.order.domain.OrderEventStatus;
import com.watermelon.server.event.order.exception.NotDuringEventPeriodException;
import com.watermelon.server.event.order.exception.WrongOrderEventFormatException;
import com.watermelon.server.event.order.result.domain.OrderResult;
import lombok.*;
import org.redisson.api.RSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class CurrentOrderEventManageService {
    private OrderEvent currentOrderEvent;

    private boolean applyFull;
    private final RSet<String> applyTickets;
//    @Setter
//    @Getter
//    private int maxWinnerCount;
//    private Long eventId;
//    private Long quizId;
//    private String answer;
//    private LocalDateTime startDate;
//    private LocalDateTime endDate;
//    @Getter
//    private final RSet<OrderResult> orderResultRset;



    @Transactional
    public void saveOrderResult(OrderResult orderResult){
        applyTickets.add(orderResult.getApplyToken());
    }


    public boolean isOrderApplyNotFullThenSave(OrderResult orderResult){
        if(currentOrderEvent.getWinnerCount()-getCurrentCount()>0){
            saveOrderResult(orderResult);
            return true;
        }
        this.currentOrderEvent.setOrderEventStatus(OrderEventStatus.CLOSED);
        return false;
    }
    public int getCurrentCount() {
        return applyTickets.size();
    }

    @Transactional
    public void refreshOrderEventInProgress(OrderEvent newOrderEvent){
        if(currentOrderEvent != null && newOrderEvent.getId().equals(currentOrderEvent.getId())){ //이미 같은 이벤트라면
            if(this.applyTickets.size()<currentOrderEvent.getWinnerCount()){
                this.currentOrderEvent.setOrderEventStatus(OrderEventStatus.OPEN);
            }
            return;
        }
        //이벤트 ID가 바꼈다면
//        this.eventId = newOrderEvent.getId();
//        this.quizId =newOrderEvent.getQuiz().getId();
//        this.answer = newOrderEvent.getQuiz().getAnswer();
//        this.startDate = newOrderEvent.getStartDate();
//        this.endDate = newOrderEvent.getEndDate();
//        this.maxWinnerCount = newOrderEvent.getWinnerCount();
        currentOrderEvent = newOrderEvent;
        this.currentOrderEvent.setOrderEventStatus(OrderEventStatus.OPEN);
        clearOrderResultRepository();
    }
    public void clearOrderResultRepository() {
        this.applyTickets.clear();
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
        if(currentOrderEvent.getOrderEventStatus().equals(OrderEventStatus.OPEN))return false;
        return true;
    }
}
