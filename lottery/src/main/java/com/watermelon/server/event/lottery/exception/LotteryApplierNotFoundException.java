package com.watermelon.server.event.lottery.exception;

public class LotteryApplierNotFoundException extends RuntimeException{

    public LotteryApplierNotFoundException() {
        super("응모자 정보를 찾을 수 없습니다.");
    }

    public LotteryApplierNotFoundException(String message) {
        super(message);
    }

    public LotteryApplierNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public LotteryApplierNotFoundException(Throwable cause) {
        super(cause);
    }

    protected LotteryApplierNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
