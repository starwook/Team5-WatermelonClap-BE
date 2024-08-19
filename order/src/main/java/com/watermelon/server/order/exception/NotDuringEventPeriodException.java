
package com.watermelon.server.order.exception;

public class NotDuringEventPeriodException extends Exception {
    public NotDuringEventPeriodException() {
        super("Not during event period");
    }
}
