package com.watermelon.server.event.lottery.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class RequestExpectationDto {
    private String expectation;




    public static RequestExpectationDto makeExpectation(String expectation) {
        return RequestExpectationDto.builder()
                .expectation(expectation)
                .build();
    }

    @Builder
    public RequestExpectationDto(String expectation) {
        this.expectation = expectation;
    }
}
