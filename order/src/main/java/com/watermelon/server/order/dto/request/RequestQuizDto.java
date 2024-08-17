package com.watermelon.server.order.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestQuizDto {

    private String answer;

    public static RequestQuizDto makeForTest(){
        return RequestQuizDto.builder()
                .answer("퍼포먼스")
                .build();
    }

}
