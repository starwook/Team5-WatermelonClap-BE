package com.watermelon.server.admin.exception;

public class EventDurationConflictException extends Exception{
    public EventDurationConflictException() {
        super("event duration conflict");
    }
}
