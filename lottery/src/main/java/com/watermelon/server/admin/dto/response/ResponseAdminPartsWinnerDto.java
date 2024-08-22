package com.watermelon.server.admin.dto.response;

import com.watermelon.server.event.lottery.domain.AdminCheckStatus;
import com.watermelon.server.event.lottery.domain.LotteryApplier;
import com.watermelon.server.event.parts.domain.PartsReward;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseAdminPartsWinnerDto {

    private String uid;
    private String name;
    private String phoneNumber;
    private String email;
    private String address;
    private int rank;
    private AdminCheckStatus status;
    private String reward;

    public static ResponseAdminPartsWinnerDto createTestDto() {

        return ResponseAdminPartsWinnerDto.builder()
                .uid("uid")
                .name("name")
                .phoneNumber("phoneNumber")
                .email("email")
                .address("address")
                .rank(1)
                .reward("reward")
                .status(AdminCheckStatus.READY)
                .build();

    }

    public static ResponseAdminPartsWinnerDto from(LotteryApplier lotteryApplier){

        return ResponseAdminPartsWinnerDto.builder()
                .uid(lotteryApplier.getUid())
                .name(lotteryApplier.getName())
                .phoneNumber(lotteryApplier.getPhoneNumber())
                .email(lotteryApplier.getEmail())
                .address(lotteryApplier.getAddress())
                .rank(1)
                .reward("아반떼 N 미니어처")
                .status(AdminCheckStatus.getStatus(lotteryApplier.isPartsWinnerCheckedByAdmin()))
                .build();

    }

}