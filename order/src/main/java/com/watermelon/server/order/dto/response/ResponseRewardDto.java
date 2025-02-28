package com.watermelon.server.order.dto.response;

import com.watermelon.server.order.domain.OrderEventReward;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ResponseRewardDto {
    private Long rewardId;
    private String name;
    private String imgSrc;

    public static ResponseRewardDto fromReward(OrderEventReward orderEventReward){
        return ResponseRewardDto.builder()
                .rewardId(orderEventReward.getId())
                .name(orderEventReward.getName())
                .imgSrc(orderEventReward.getImgSrc())
                .build();
    }

    @Override
    public String toString() {
        return "ResponseRewardDto{" +
                "rewardId=" + rewardId +
                ", name='" + name + '\'' +
                ", imgSrc='" + imgSrc + '\'' +
                '}';
    }
}
