package com.watermelon.server.admin.dto.response;

import com.watermelon.server.event.lottery.domain.AdminCheckStatus;
import com.watermelon.server.event.lottery.domain.LotteryApplier;
import com.watermelon.server.event.lottery.domain.LotteryReward;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseAdminLotteryWinnerDto {

    private String uid;
    private String name;
    private String phoneNumber;
    private String email;
    private String address;
    private int rank;
    private AdminCheckStatus status;
    private String reward;

    public static ResponseAdminLotteryWinnerDto createTestDto() {

        return ResponseAdminLotteryWinnerDto.builder()
                .uid("uid")
                .name("name")
                .phoneNumber("phoneNumber")
                .email("email")
                .address("address")
                .rank(1)
                .status(AdminCheckStatus.READY)
                .reward("reward")
                .build();

    }

    public static ResponseAdminLotteryWinnerDto from(LotteryApplier lotteryApplier, LotteryReward lotteryReward){

        return ResponseAdminLotteryWinnerDto.builder()
                .uid(lotteryApplier.getUid())
                .name(lotteryApplier.getName())
                .phoneNumber(lotteryApplier.getPhoneNumber())
                .email(lotteryApplier.getEmail())
                .address(lotteryApplier.getAddress())
                .rank(lotteryApplier.getLotteryRank())
                .status(AdminCheckStatus.getStatus(lotteryApplier.isLotteryWinnerCheckedByAdmin()))
                .reward(lotteryReward.getName())
                .build();

    }

}