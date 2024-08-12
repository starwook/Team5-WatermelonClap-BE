package com.watermelon.server.event.lottery.dto.request;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class RequestLotteryEventDto {

    private String name;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private List<RequestLotteryRewardDto> rewards;

    private class RequestLotteryRewardDto {

        private int rank;
        private int winnerCount;
        private String name;

    }

}
