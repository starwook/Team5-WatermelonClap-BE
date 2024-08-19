package com.watermelon.server.event.lottery.controller;

import com.watermelon.server.auth.annotations.Uid;
import com.watermelon.server.event.lottery.dto.request.RequestLotteryWinnerInfoDto;
import com.watermelon.server.event.lottery.dto.response.ResponseLotteryRankDto;
import com.watermelon.server.event.lottery.dto.response.ResponseLotteryWinnerDto;
import com.watermelon.server.event.lottery.dto.response.ResponseLotteryWinnerInfoDto;
import com.watermelon.server.event.lottery.dto.response.ResponseRewardInfoDto;
import com.watermelon.server.event.lottery.error.ExpectationAlreadyExistError;
import com.watermelon.server.event.lottery.exception.LotteryApplierNotFoundException;
import com.watermelon.server.event.lottery.exception.LotteryRewardNotFoundException;
import com.watermelon.server.event.lottery.service.LotteryRewardService;
import com.watermelon.server.event.lottery.service.LotteryService;
import com.watermelon.server.event.lottery.service.LotteryWinnerService;
import com.watermelon.server.exception.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/event/lotteries")
public class LotteryController {

    private final LotteryService lotteryService;
    private final LotteryWinnerService lotteryWinnerService;
    private final LotteryRewardService lotteryRewardService;

    @GetMapping
    public ResponseEntity<List<ResponseLotteryWinnerDto>> getLotteryWinnerList(){
        return new ResponseEntity<>(lotteryWinnerService.getLotteryWinners(), HttpStatus.OK);
    }

    @GetMapping("/info")
    public ResponseEntity<ResponseLotteryWinnerInfoDto> getLotteryWinnerInfo(
            @Uid String uid
    ){
        return new ResponseEntity<>(lotteryWinnerService.getLotteryWinnerInfo(uid), HttpStatus.OK);
    }

    @PostMapping("/info")
    public ResponseEntity<Void> createLotteryWinnerInfo(
            @RequestBody RequestLotteryWinnerInfoDto dto,
            @Uid String uid
    ){
        lotteryWinnerService.createLotteryWinnerInfo(uid, dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/rank")
    public ResponseEntity<ResponseLotteryRankDto> getLotteryRank(
            @Uid String uid
    ){
        return new ResponseEntity<>(lotteryService.getLotteryRank(uid), HttpStatus.OK);
    }

    @GetMapping("/reward/{rank}")
    public ResponseEntity<ResponseRewardInfoDto> getRewardInfo(
            @PathVariable int rank
    ){
        return new ResponseEntity<>(lotteryRewardService.getRewardInfo(rank), HttpStatus.OK);
    }

    @ExceptionHandler(LotteryApplierNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleLotteryApplierNotFoundException(LotteryApplierNotFoundException lotteryApplierNotFoundException){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.of(lotteryApplierNotFoundException.getMessage()));
    }

    @ExceptionHandler(LotteryRewardNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleLotteryRewardNotFoundException(LotteryRewardNotFoundException lotteryRewardNotFoundException){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.of(lotteryRewardNotFoundException.getMessage()));
    }

}
