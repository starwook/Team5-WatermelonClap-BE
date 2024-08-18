package com.watermelon.server.event.lottery.exception;

public class LotteryRewardNotFoundException extends RuntimeException{

    public LotteryRewardNotFoundException() {
        super("서버에 등록된 추첨이벤트 경품이 없습니다.");
    }

    public LotteryRewardNotFoundException(String message) {
        super(message);
    }

    public LotteryRewardNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public LotteryRewardNotFoundException(Throwable cause) {
        super(cause);
    }

    protected LotteryRewardNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
