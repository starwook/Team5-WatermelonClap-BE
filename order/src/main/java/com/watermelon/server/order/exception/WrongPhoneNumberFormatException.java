
package com.watermelon.server.order.exception;

public class WrongPhoneNumberFormatException extends Exception{
    public WrongPhoneNumberFormatException() {
        super("Invalid phone number");
    }
}
