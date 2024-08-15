package com.watermelon.server.event.order.error;

public class WinnerAlreadyParticipateException extends Exception{
    public WinnerAlreadyParticipateException(){
        super("Winner already Participate");
    }
}
