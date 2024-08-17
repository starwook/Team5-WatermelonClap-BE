package com.watermelon.server.order.error;

public class WrongPhoneNumberFormatException extends Exception{
    public WrongPhoneNumberFormatException() {
        super("Invalid phone number");
    }
}
