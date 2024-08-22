package com.watermelon.server.admin.dto.response;

import com.watermelon.server.event.lottery.domain.LotteryEvent;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class ResponseLotteryEventDto {

    private String name;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public static List<ResponseLotteryEventDto> from(List<LotteryEvent> lotteryEvents) {
        return lotteryEvents.stream().map(
                lotteryEvent -> new ResponseLotteryEventDto(
                        lotteryEvent.getName(),
                        lotteryEvent.getStartTime(),
                        lotteryEvent.getEndTime()
                        )
        ).toList();
    }

}
