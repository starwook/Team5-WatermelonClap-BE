package com.watermelon.server.order.exception;

public class EventDurationConflictException extends Exception{
    public EventDurationConflictException() {
        super("event duration conflict");
    }
}
