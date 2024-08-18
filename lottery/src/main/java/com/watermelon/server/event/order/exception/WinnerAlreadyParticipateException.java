package com.watermelon.server.event.order.exception;

public class WinnerAlreadyParticipateException extends Exception{
    public WinnerAlreadyParticipateException(){
        super("Winner already Participate");
    }
}
