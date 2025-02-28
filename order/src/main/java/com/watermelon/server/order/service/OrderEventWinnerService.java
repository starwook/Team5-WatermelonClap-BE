package com.watermelon.server.order.service;


import com.watermelon.server.order.exception.ApplyTicketWrongException;
import com.watermelon.server.order.domain.OrderEvent;
import com.watermelon.server.order.domain.OrderEventWinner;
import com.watermelon.server.order.dto.request.OrderEventWinnerRequestDto;
import com.watermelon.server.order.exception.WinnerAlreadyParticipateException;
import com.watermelon.server.order.exception.WrongPhoneNumberFormatException;
import com.watermelon.server.order.repository.OrderEventRepository;
import com.watermelon.server.order.repository.OrderEventWinnerRepository;

import com.watermelon.server.token.ApplyTokenProvider;
import com.watermelon.server.token.JwtPayload;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderEventWinnerService {
    private static final Logger log = LoggerFactory.getLogger(OrderEventWinnerService.class);
    private final OrderEventWinnerRepository orderEventWinnerRepository;
    private final ApplyTokenProvider applyTokenProvider;
    private final OrderEventRepository orderEventRepository;

    @Transactional
    public OrderEventWinner makeWinner(
            OrderEvent orderEvent,
            OrderEventWinnerRequestDto orderEventWinnerRequestDto,
            String applyAnswer,
            String applyTicket)
            throws ApplyTicketWrongException, WrongPhoneNumberFormatException, WinnerAlreadyParticipateException {
        if(!orderEventWinnerRequestDto.isPhoneNumberValid()) throw new WrongPhoneNumberFormatException();
        JwtPayload payload = applyTokenProvider.verifyToken(applyTicket, String.valueOf(orderEvent.getId()));
        OrderEventWinner orderEventWinner = OrderEventWinner.makeWinner(orderEvent
                , orderEventWinnerRequestDto
        ,applyAnswer,applyTicket);
        Optional<OrderEventWinner> savedOrderEventWinner = orderEventWinnerRepository.findByApplyTicket(applyTicket);
        if(savedOrderEventWinner.isPresent()) throw new WinnerAlreadyParticipateException();
        //ApplyTicket  payload에서 applyAnswer를 담도록 하여야함
        return orderEventWinnerRepository.save(orderEventWinner);
    }
}
