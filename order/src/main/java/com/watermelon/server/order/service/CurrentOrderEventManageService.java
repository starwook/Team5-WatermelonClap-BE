package com.watermelon.server.order.service;

import com.watermelon.server.order.domain.OrderEvent;

import com.watermelon.server.order.domain.OrderEventStatus;
import com.watermelon.server.order.exception.NotDuringEventPeriodException;
import com.watermelon.server.order.exception.WrongOrderEventFormatException;
import com.watermelon.server.order.repository.OrderResultRepository;
import com.watermelon.server.order.result.domain.OrderResult;
import lombok.*;
import org.redisson.api.RSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class CurrentOrderEventManageService {
    @Getter
    private OrderEvent currentOrderEvent;

//    private final RSet<String> applyTickets;
    private final OrderResultRepository orderResultRepository;

    public void saveOrderResult(OrderResult orderResult){
//        applyTickets.add(orderResult.getApplyToken());

        orderResultRepository.save(orderResult);
    }


    public boolean isOrderApplyNotFullThenSave(OrderResult orderResult){
        if(currentOrderEvent.getWinnerCount()- getCurrentApplyTicketSize()>0){
            saveOrderResult(orderResult);
            return true;
        }
        // 여기서 CLOSED로 바꿀지 언정 실제 DB에는 저장되지 않음(currentOrderEvent는 DB에서 꺼내온 정보가 아님)
        // 이 CLOSED는 REDIS를 읽는 작업을 줄여주기 위한 변수용
        this.currentOrderEvent.setOrderEventStatus(OrderEventStatus.CLOSED);
        return false;
    }
    public int getCurrentApplyTicketSize() {
        return orderResultRepository.findAll().size();
    }


    @Transactional
    public void refreshOrderEventInProgress(OrderEvent orderEventFromDB){
        //동일한 이벤트라면
        if(currentOrderEvent != null && orderEventFromDB.getId().equals(currentOrderEvent.getId())){
            if(getCurrentApplyTicketSize()<currentOrderEvent.getWinnerCount()){
                this.currentOrderEvent.setOrderEventStatus(OrderEventStatus.OPEN);
            }
            //실제 DB에 CLOSED로 바꾸어주는 메소드는 이곳 (스케쥴링)
            orderEventFromDB.setOrderEventStatus(currentOrderEvent.getOrderEventStatus());
            return;
        }

        currentOrderEvent = orderEventFromDB;
        clearOrderResultRepository();
    }
    public void clearOrderResultRepository() {
         orderResultRepository.deleteAll();
//        this.applyTickets.clear();
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
