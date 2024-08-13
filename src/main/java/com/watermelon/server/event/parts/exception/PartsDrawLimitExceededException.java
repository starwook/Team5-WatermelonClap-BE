package com.watermelon.server.event.parts.exception;

public class PartsDrawLimitExceededException extends RuntimeException {
    public PartsDrawLimitExceededException() {
        super("Part draw limit exceeded");
    }
}
