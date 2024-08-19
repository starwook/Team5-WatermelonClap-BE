package com.watermelon.server.event.lottery.dto.response;

import com.watermelon.server.event.lottery.domain.LotteryApplier;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseLotteryRankDto {

    private int rank;

    private boolean isApplied;

    private boolean miniature;

    public static ResponseLotteryRankDto from(LotteryApplier lotteryApplier) {
        return ResponseLotteryRankDto.builder()
                .rank(lotteryApplier.getLotteryRank())
                .isApplied(lotteryApplier.isLotteryApplier())
                .miniature(lotteryApplier.isPartsWinner())
                .build();
    }

    public static ResponseLotteryRankDto createNotApplied(){
        return ResponseLotteryRankDto.builder()
                .rank(-1)
                .isApplied(false)
                .miniature(false)
                .build();
    }

    public static ResponseLotteryRankDto createLotteryWinnerTest(){
        return ResponseLotteryRankDto.builder()
                .rank(1)
                .isApplied(true)
                .miniature(false)
                .build();
    }

    public static ResponseLotteryRankDto createAppliedButNotWinnerTest(){
        return ResponseLotteryRankDto.builder()
                .rank(-1)
                .isApplied(true)
                .miniature(false)
                .build();
    }

    public static ResponseLotteryRankDto createPartsWinnerTest(){
        return ResponseLotteryRankDto.builder()
                .rank(-1)
                .isApplied(true)
                .miniature(true)
                .build();
    }

    public static ResponseLotteryRankDto createPartsAndLotteryWinnerTest(){
        return ResponseLotteryRankDto.builder()
                .rank(1)
                .isApplied(true)
                .miniature(true)
                .build();
    }

}
