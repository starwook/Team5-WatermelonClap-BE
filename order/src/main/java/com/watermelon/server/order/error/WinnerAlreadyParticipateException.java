package com.watermelon.server.order.error;

public class WinnerAlreadyParticipateException extends Exception{
    public WinnerAlreadyParticipateException(){
        super("Winner already Participate");
    }
}
