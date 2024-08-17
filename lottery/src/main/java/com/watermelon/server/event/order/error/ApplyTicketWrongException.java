package com.watermelon.server.event.order.error;



public class ApplyTicketWrongException extends Exception{
    public ApplyTicketWrongException(){
        super("apply ticket not verified");
    }
}
