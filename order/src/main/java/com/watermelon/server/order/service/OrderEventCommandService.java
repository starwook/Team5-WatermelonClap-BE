package com.watermelon.server.order.service;



import com.watermelon.server.order.exception.ApplyTicketWrongException;
import com.watermelon.server.order.dto.request.OrderEventWinnerRequestDto;
import com.watermelon.server.order.exception.WinnerAlreadyParticipateException;
import com.watermelon.server.order.exception.WrongPhoneNumberFormatException;
import com.watermelon.server.order.exception.WrongOrderEventFormatException;
import com.watermelon.server.order.domain.OrderEvent;
import com.watermelon.server.order.repository.OrderEventRepository;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderEventCommandService {

    private final OrderEventRepository orderEventRepository;
    private final OrderEventWinnerService orderEventWinnerService;
    private final CurrentOrderEventManageService currentOrderEventManageService;



    public OrderEventCommandService(
            OrderEventRepository orderEventRepository,
            OrderEventWinnerService orderEventWinnerService,
            CurrentOrderEventManageService currentOrderEventManageService) {
        this.orderEventRepository = orderEventRepository;
        this.orderEventWinnerService = orderEventWinnerService;
        this.currentOrderEventManageService = currentOrderEventManageService;
        findOrderEventToMakeInProgress();
    }


    @Transactional
    public void makeOrderEventWinner(String applyTicket, Long eventId, OrderEventWinnerRequestDto orderEventWinnerRequestDto) throws ApplyTicketWrongException, WrongOrderEventFormatException, WrongPhoneNumberFormatException, WinnerAlreadyParticipateException {
        OrderEvent orderEvent = orderEventRepository.findById(eventId).orElseThrow(WrongOrderEventFormatException::new);
        orderEventWinnerService.makeWinner(orderEvent, orderEventWinnerRequestDto,orderEvent.getQuiz().getAnswer(),applyTicket);
    }

    @Transactional
    public Long findOrderEventToMakeInProgress(){
        List<OrderEvent> orderEvents = orderEventRepository.findAll();
        if(orderEvents.isEmpty()) return currentOrderEventManageService.getCurrentOrderEventId();// 이벤트 없을시 스킵

        for(OrderEvent orderEvent : orderEvents){
            //현재 이벤트중 시간에 맞는 이벤트가 있다면 현재 이벤트로 설정한다
            if(orderEvent.isTimeInEventTime(LocalDateTime.now())){
                this.currentOrderEventManageService.refreshOrderEventInProgress(orderEvent);
            }
        }
        return currentOrderEventManageService.getCurrentOrderEventId();
    }
}
