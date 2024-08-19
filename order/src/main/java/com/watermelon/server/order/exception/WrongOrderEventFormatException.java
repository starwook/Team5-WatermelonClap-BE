
package com.watermelon.server.order.exception;


public class WrongOrderEventFormatException extends Exception{
    public WrongOrderEventFormatException() {
        super("Wrong order event format");
    }

}
