package com.watermelon.server.event.parts.exception;

public class PartsNotExistException extends RuntimeException {
    public PartsNotExistException() {
        super("해당 파츠가 존재하지 않습니다.");
    }

    public PartsNotExistException(String message) {
        super(message);
    }

    public PartsNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public PartsNotExistException(Throwable cause) {
        super(cause);
    }

    protected PartsNotExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
