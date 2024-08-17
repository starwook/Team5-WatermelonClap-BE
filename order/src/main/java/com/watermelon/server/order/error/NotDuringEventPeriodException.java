package com.watermelon.server.order.error;

public class NotDuringEventPeriodException extends Exception {
    public NotDuringEventPeriodException() {
        super("Not during event period");
    }
}
