package com.watermelon.server.event.parts.exception;

public class PartsDrawLimitExceededException extends RuntimeException {
    public PartsDrawLimitExceededException() {
        super("파츠 뽑기 횟수를 초과했습니다.");
    }
}
