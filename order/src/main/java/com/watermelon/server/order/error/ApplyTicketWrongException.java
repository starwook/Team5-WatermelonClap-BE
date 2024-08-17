package com.watermelon.server.order.error;



public class ApplyTicketWrongException extends Exception{
    public ApplyTicketWrongException(){
        super("apply ticket not verified");
    }
}
