package com.watermelon.server.event.order.service;


import com.watermelon.server.error.ApplyTicketWrongException;
import com.watermelon.server.event.order.dto.request.OrderEventWinnerRequestDto;
import com.watermelon.server.event.order.dto.request.RequestAnswerDto;
import com.watermelon.server.event.order.dto.response.ResponseApplyTicketDto;
import com.watermelon.server.event.order.error.NotDuringEventPeriodException;
import com.watermelon.server.event.order.error.WrongPhoneNumberFormatException;
import com.watermelon.server.event.order.error.WrongOrderEventFormatException;
import com.watermelon.server.event.order.domain.OrderEvent;
import com.watermelon.server.event.order.repository.OrderEventRepository;
import com.watermelon.server.event.order.result.service.OrderResultCommandService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderEventCommandService {

    private final OrderEventRepository orderEventRepository;
    private final OrderEventWinnerService orderEventWinnerService;
    private final OrderEventCheckService orderEventCheckService;

    public OrderEventCommandService(
            OrderEventRepository orderEventRepository,
            OrderEventWinnerService orderEventWinnerService,
            OrderEventCheckService orderEventCheckService) {
        this.orderEventRepository = orderEventRepository;
        this.orderEventWinnerService = orderEventWinnerService;
        this.orderEventCheckService = orderEventCheckService;
        findOrderEventToMakeInProgress();
    }
    @Transactional
    public void makeOrderEventWinner(String applyTicket, Long eventId, OrderEventWinnerRequestDto orderEventWinnerRequestDto) throws ApplyTicketWrongException, WrongOrderEventFormatException, WrongPhoneNumberFormatException {
        OrderEvent orderEvent = orderEventRepository.findById(eventId).orElseThrow(WrongOrderEventFormatException::new);
        orderEventWinnerService.makeWinner(orderEvent, orderEventWinnerRequestDto,"payLoad.applyAnswer",applyTicket);
    }

    @Transactional
    public Long findOrderEventToMakeInProgress(){
        //현재 OrderEvent의 상태를 주기적으로 변경
        List<OrderEvent> orderEvents = orderEventRepository.findAll();
        if(orderEvents.isEmpty()) return orderEventCheckService.getCurrentOrderEventId();// 이벤트 없을시 스킵

        for(OrderEvent orderEvent : orderEvents){
            //현재 이벤트중 시간에 맞는 이벤트가 있다면 현재 이벤트로 설정한다
            if(orderEvent.isTimeInEventTime(LocalDateTime.now())){
                this.orderEventCheckService.refreshOrderEventInProgress(orderEvent);
                return orderEvent.getId();
            }
        }
        return orderEventCheckService.getCurrentOrderEventId();
    }
}
