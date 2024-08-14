package com.watermelon.server.event.order.service;

import com.watermelon.server.event.order.domain.OrderEvent;
import com.watermelon.server.event.order.error.NotDuringEventPeriodException;
import com.watermelon.server.event.order.error.WrongOrderEventFormatException;
import com.watermelon.server.event.order.result.domain.OrderResult;
import lombok.*;
import org.redisson.api.RSet;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class CurrentOrderEventManageService {
    private Long eventId;
    private Long quizId;
    private String answer;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    @Setter
    @Getter
    private int maxWinnerCount;
    @Getter
    private final RSet<OrderResult> orderResultRset;
    private final RSet<String> applyTickets;

    public void saveOrderResult(OrderResult orderResult){
        applyTickets.add(orderResult.getApplyToken());
    }
    public boolean isOrderApplyNotFullThenSave(OrderResult orderResult){
        if(maxWinnerCount-getCurrentCount()>0){
            saveOrderResult(orderResult);
            return true;
        }
        return false;
    }
    public int getCurrentCount() {
        return applyTickets.size();
    }

    public void refreshOrderEventInProgress(OrderEvent orderEvent){
        if(orderEvent.getId().equals(this.eventId)){
            return;
        }
        this.eventId = orderEvent.getId();
        this.quizId =orderEvent.getQuiz().getId();
        this.answer = orderEvent.getQuiz().getAnswer();
        this.startDate = orderEvent.getStartDate();
        this.endDate = orderEvent.getEndDate();
        this.maxWinnerCount = orderEvent.getWinnerCount();
        clearOrderResultRepository();
    }
    public void clearOrderResultRepository() {
        this.applyTickets.clear();
    }



    public boolean isAnswerCorrect(String submitAnswer){
        if(this.answer.equals(submitAnswer)) return true;
        return false;
    }
    public boolean isTimeInEvent(LocalDateTime now){
        if(now.isAfter(this.startDate) &&now.isBefore(endDate)) return true;
        return false;
    }
    public boolean isEventAndQuizIdWrong( Long eventId,Long quizId) {
        if(this.eventId !=null && this.eventId.equals(eventId) && this.quizId.equals(quizId)) return true;
        return false;
    }
    public void checkingInfoErrors( Long eventId, Long quizId)
            throws WrongOrderEventFormatException, NotDuringEventPeriodException {
        if (!isEventAndQuizIdWrong(eventId, quizId)) throw new WrongOrderEventFormatException();
        if (!isTimeInEvent(LocalDateTime.now())) throw new NotDuringEventPeriodException();
    }
    public Long getCurrentOrderEventId() {
        return eventId;
    }


}
