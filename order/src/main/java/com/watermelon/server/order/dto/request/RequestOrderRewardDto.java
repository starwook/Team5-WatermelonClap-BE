package com.watermelon.server.order.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestOrderRewardDto {

    private String name;
    public static RequestOrderRewardDto makeForTest(){
        return RequestOrderRewardDto.builder()
                .name("testName")
                .build();
    }
}
