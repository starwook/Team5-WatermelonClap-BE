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

    public static RequestLotteryEventDto createTest(){
        RequestLotteryEventDto dto = new RequestLotteryEventDto();
        dto.name = "name";
        dto.startTime = LocalDateTime.now();
        dto.endTime = LocalDateTime.now().plusDays(1);
        dto.rewards = List.of(
                RequestLotteryRewardDto.createTest()
        );
        return dto;
    }

    @Data
    public static class RequestLotteryRewardDto {

        private int rank;
        private int winnerCount;
        private String name;

        public static RequestLotteryRewardDto createTest(){
            RequestLotteryRewardDto dto = new RequestLotteryRewardDto();
            dto.rank = 1;
            dto.winnerCount = 1;
            dto.name = "name";
            return dto;
        }
    }
}
