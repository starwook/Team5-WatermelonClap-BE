package com.watermelon.server.exception;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Global Exception Handler에서 발생한 에러에 대한 응답 처리를 관리
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {
    private String reason;

    public static ErrorResponse of(String reason){
        return ErrorResponse.builder()
                .reason(reason)
                .build();
    }
    @Builder
    public ErrorResponse(String reason) {
        this.reason = reason;
    }
}