package com.watermelon.server.event.order.exception;

public class WrongPhoneNumberFormatException extends Exception{
    public WrongPhoneNumberFormatException() {
        super("Invalid phone number");
    }
}
