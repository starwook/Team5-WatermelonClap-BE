package com.watermelon.server.event.order.controller;


import com.watermelon.server.common.exception.ErrorResponse;
import com.watermelon.server.error.ApplyTicketWrongException;
import com.watermelon.server.event.order.dto.request.OrderEventWinnerRequestDto;
import com.watermelon.server.event.order.dto.request.RequestAnswerDto;
import com.watermelon.server.event.order.dto.response.ResponseApplyTicketDto;
import com.watermelon.server.event.order.dto.response.ResponseOrderEventDto;
import com.watermelon.server.event.order.error.NotDuringEventPeriodException;
import com.watermelon.server.event.order.error.WinnerAlreadyParticipateException;
import com.watermelon.server.event.order.error.WrongPhoneNumberFormatException;
import com.watermelon.server.event.order.error.WrongOrderEventFormatException;
import com.watermelon.server.event.order.result.service.OrderResultCommandService;
import com.watermelon.server.event.order.service.OrderEventCommandService;
import com.watermelon.server.event.order.service.OrderEventQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderEventController {

    private final OrderEventQueryService orderEventQueryService;
    private final OrderEventCommandService orderEventCommandService;
    private final OrderResultCommandService orderResultCommandService;

    @GetMapping(path = "/event/order")
    public List<ResponseOrderEventDto> getOrderEvents(){

        return orderEventQueryService.getOrderEvents();
    }
//    @PostMapping(path = "/apply")
//    public ResponseQuizResultDto applyFifoEvent(@RequestBody RequestAnswerDto requestAnswerDto){
//        return fifoEventService.applyFifoEvent(requestAnswerDto);
//    }

//    @Cacheable("orderEvents")
    @GetMapping(path = "/event/order/{eventId}")
    public ResponseOrderEventDto getOrderEvent(@PathVariable("eventId") Long orderEventId) throws WrongOrderEventFormatException {
        return orderEventQueryService.getOrderEvent(orderEventId);
    }

    @PostMapping(path = "/event/order/{eventId}/{quizId}")
    public ResponseApplyTicketDto makeApplyTicket(@RequestBody RequestAnswerDto requestAnswerDto,
                                                  @PathVariable("eventId") Long orderEventId,
                                                  @PathVariable("quizId") Long quizId)
            throws WrongOrderEventFormatException, NotDuringEventPeriodException {

        return orderResultCommandService.makeApplyTicket(requestAnswerDto,orderEventId,quizId);
    }

    @PostMapping(path = "/event/order/{eventId}/{quizId}/apply")
    public void makeApply(@RequestHeader("ApplyTicket") String applyTicket,
                          @PathVariable("eventId") Long eventId,
                          @PathVariable("quizId") Long quizId,
                          @RequestBody OrderEventWinnerRequestDto orderEventWinnerRequestDto)
            throws ApplyTicketWrongException
            , WrongOrderEventFormatException
            , WrongPhoneNumberFormatException, WinnerAlreadyParticipateException {
        orderEventCommandService.makeOrderEventWinner(applyTicket,eventId,orderEventWinnerRequestDto);
    }

    @ExceptionHandler(WrongPhoneNumberFormatException.class)
    public ResponseEntity<ErrorResponse> handleWrongPhoneNumberFormatException(WrongPhoneNumberFormatException wrongPhoneNumberFormatException){
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(ErrorResponse.of(wrongPhoneNumberFormatException.getMessage()));
    }
    @ExceptionHandler(WrongOrderEventFormatException.class)
    public ResponseEntity<ErrorResponse> handleWrongOrderEventFormatException(WrongOrderEventFormatException wrongOrderEventFormatException){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.of(wrongOrderEventFormatException.getMessage()));
    }
    @ExceptionHandler(ApplyTicketWrongException.class)
    public ResponseEntity<ErrorResponse> handleApplyTicketWrongException(ApplyTicketWrongException applyTicketWrongException){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponse.of(applyTicketWrongException.getMessage()));
    }
    @ExceptionHandler(NotDuringEventPeriodException.class)
    public ResponseEntity<ErrorResponse> handleNotDuringEventPeriodException(NotDuringEventPeriodException notDuringEventPeriodException){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ErrorResponse.of(notDuringEventPeriodException.getMessage()));
    }
    @ExceptionHandler(WinnerAlreadyParticipateException.class)
    public ResponseEntity<ErrorResponse> handleWinnerAlreadyParticipateException(WinnerAlreadyParticipateException winnerAlreadyParticipateException){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponse.of(winnerAlreadyParticipateException.getMessage()));
    }

}
