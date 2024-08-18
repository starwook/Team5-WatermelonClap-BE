package com.watermelon.server.event.order.exception;

public class NotDuringEventPeriodException extends Exception {
    public NotDuringEventPeriodException() {
        super("Not during event period");
    }
}
