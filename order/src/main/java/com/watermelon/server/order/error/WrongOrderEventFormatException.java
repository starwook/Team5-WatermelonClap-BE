package com.watermelon.server.order.error;

public class WrongOrderEventFormatException extends Exception{
    public WrongOrderEventFormatException() {
        super("Wrong order event format");
    }

}
