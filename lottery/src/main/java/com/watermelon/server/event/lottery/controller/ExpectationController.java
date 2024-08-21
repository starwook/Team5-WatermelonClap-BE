package com.watermelon.server.event.lottery.controller;

import com.watermelon.server.event.lottery.dto.response.ResponseExpectationCheckDto;
import com.watermelon.server.exception.ErrorResponse;
import com.watermelon.server.auth.annotations.Uid;
import com.watermelon.server.event.lottery.dto.request.RequestExpectationDto;
import com.watermelon.server.event.lottery.dto.response.ResponseExpectationDto;
import com.watermelon.server.event.lottery.error.ExpectationAlreadyExistError;
import com.watermelon.server.event.lottery.service.ExpectationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ExpectationController {

    private final ExpectationService expectationService;


    @PostMapping(path = "/expectations")
    public ResponseEntity<Void> makeExpectation(
            @RequestBody RequestExpectationDto requestExpectationDto,
            @Uid String uid
    ) throws ExpectationAlreadyExistError {
        expectationService.makeExpectation(uid,requestExpectationDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping(path ="/expectations")
    public ResponseEntity<List<ResponseExpectationDto>> getExpectationsForUser() {
        return new ResponseEntity<>(expectationService.getExpectationsForUser(),HttpStatus.OK);
    }

    @GetMapping(path = "/expectations/check")
    public ResponseEntity<ResponseExpectationCheckDto> getExpectationCheck(
            @Uid String uid
    ) {
       ResponseExpectationCheckDto responseExpectationCheckDto = expectationService.isExpectationAlreadyExist(uid);
       log.info(uid);
       log.info(responseExpectationCheckDto.toString());
        return new ResponseEntity<>(responseExpectationCheckDto, HttpStatus.OK);
    }

    @ExceptionHandler(ExpectationAlreadyExistError.class)
    public ResponseEntity<ErrorResponse> handleNotDuringEventPeriodException(ExpectationAlreadyExistError expectationAlreadyExistError){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponse.of(expectationAlreadyExistError.getMessage()));
    }

}
