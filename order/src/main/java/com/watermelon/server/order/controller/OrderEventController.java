package com.watermelon.server.order.controller;


import com.watermelon.server.exception.ErrorResponse;
import com.watermelon.server.order.exception.ApplyTicketWrongException;
import com.watermelon.server.order.dto.request.OrderEventWinnerRequestDto;
import com.watermelon.server.order.dto.request.RequestAnswerDto;
import com.watermelon.server.order.dto.response.ResponseApplyTicketDto;
import com.watermelon.server.order.dto.response.ResponseOrderEventDto;
import com.watermelon.server.order.exception.NotDuringEventPeriodException;
import com.watermelon.server.order.exception.WinnerAlreadyParticipateException;
import com.watermelon.server.order.exception.WrongPhoneNumberFormatException;
import com.watermelon.server.order.exception.WrongOrderEventFormatException;
import com.watermelon.server.order.service.orderApply.OrderApplyService;
import com.watermelon.server.order.service.OrderEventCommandService;
import com.watermelon.server.order.service.OrderEventQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderEventController {

    private final OrderEventQueryService orderEventQueryService;
    private final OrderEventCommandService orderEventCommandService;
    private final OrderApplyService orderApplyService;



    @GetMapping(path = "/event/order")
    public List<ResponseOrderEventDto> getOrderEvents(){
        return orderEventQueryService.getOrderEvents();
    }
//    @PostMapping(path = "/apply")
//    public ResponseQuizResultDto applyFifoEvent(@RequestBody RequestAnswerDto requestAnswerDto){
//        return fifoEventService.applyFifoEvent(requestAnswerDto);
//    }


    @GetMapping(path = "/event/order/{eventId}")
    public ResponseOrderEventDto getOrderEvent(@PathVariable("eventId") Long orderEventId) throws WrongOrderEventFormatException {
        return orderEventQueryService.getOrderEvent(orderEventId);
    }

    @PostMapping(path = "/event/order/{eventId}/{quizId}")
    public ResponseApplyTicketDto applyOrderEvent(@RequestBody RequestAnswerDto requestAnswerDto,
                                                  @PathVariable("eventId") Long orderEventId,
                                                  @PathVariable("quizId") Long quizId)
            throws WrongOrderEventFormatException, NotDuringEventPeriodException {

        return orderApplyService.makeApplyTicket(requestAnswerDto,orderEventId,quizId);
    }

    @PostMapping(path = "/event/order/{eventId}/{quizId}/apply")
    public void makeWinnerInformation(@RequestHeader("ApplyTicket") String applyTicket,
                                      @PathVariable("eventId") Long eventId,
                                      @PathVariable("quizId") Long quizId,
                                      @RequestBody OrderEventWinnerRequestDto orderEventWinnerRequestDto)
            throws ApplyTicketWrongException
            , WrongOrderEventFormatException
            , WrongPhoneNumberFormatException, WinnerAlreadyParticipateException {
        orderEventCommandService.makeOrderEventWinner(applyTicket,eventId,orderEventWinnerRequestDto);
    }

    @PostMapping(path="/event/order/refresh")
    public void refreshOrderEvent(){
        orderApplyService.refreshApplyCount();
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
