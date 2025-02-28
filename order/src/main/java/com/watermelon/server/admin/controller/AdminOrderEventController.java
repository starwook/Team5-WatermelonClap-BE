package com.watermelon.server.admin.controller;



import com.watermelon.server.exception.S3ImageFormatException;
import com.watermelon.server.admin.service.AdminOrderEventService;
import com.watermelon.server.exception.ErrorResponse;
import com.watermelon.server.order.dto.request.RequestOrderEventDto;
import com.watermelon.server.order.dto.response.ResponseOrderEventDto;
import com.watermelon.server.order.dto.response.ResponseOrderEventWinnerDto;
import com.watermelon.server.order.exception.EventDurationConflictException;
import com.watermelon.server.order.exception.WrongOrderEventFormatException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin")
public class AdminOrderEventController {
    // 변환 작업 필요
    private final AdminOrderEventService adminOrderEventService;
    @PostMapping("/event/order")
    public ResponseOrderEventDto makeOrderEvent(
            @RequestPart("orderEvent") @Valid RequestOrderEventDto requestOrderEventDto,
            @RequestPart("rewardImage") MultipartFile rewardImage,
            @RequestPart("quizImage") MultipartFile quizImage)
            throws S3ImageFormatException, EventDurationConflictException {

        return adminOrderEventService.makeOrderEvent(requestOrderEventDto,rewardImage,quizImage);
    }
    @DeleteMapping("/event/order/{eventId}")
    public ResponseEntity<Void> deleteOrderEvent(@PathVariable("eventId") Long eventId) throws WrongOrderEventFormatException {
        adminOrderEventService.deleteOrderEvent(eventId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/event/order")
    public List<ResponseOrderEventDto> getOrderEventForAdmin(){
        return adminOrderEventService.getOrderEventsForAdmin();
    }

    @GetMapping("/event/order/{eventId}/winner")
    public List<ResponseOrderEventWinnerDto> getOrderEventWinnersForAdmin(@PathVariable("eventId") Long eventId) throws WrongOrderEventFormatException {
        return adminOrderEventService.getOrderEventWinnersForAdmin(eventId);
    }


    @ExceptionHandler(S3ImageFormatException.class)
    public ResponseEntity<ErrorResponse> handleS3ImageFormatException(S3ImageFormatException s3ImageFormatException){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.of(s3ImageFormatException.getMessage()));
    }
    @ExceptionHandler(WrongOrderEventFormatException.class)
    public ResponseEntity<ErrorResponse> handleWrongOrderEventFormatException(WrongOrderEventFormatException wrongOrderEventFormatException){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.of(wrongOrderEventFormatException.getMessage()));
    }
    @ExceptionHandler(EventDurationConflictException.class)
    public ResponseEntity<ErrorResponse> handleEventDurationConflictException(EventDurationConflictException eventDurationConflictException){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponse.of(eventDurationConflictException.getMessage()));
    }

}
