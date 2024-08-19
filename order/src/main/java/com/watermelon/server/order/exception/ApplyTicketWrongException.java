package com.watermelon.server.order.exception;



public class ApplyTicketWrongException extends Exception{
    public ApplyTicketWrongException(){
        super("apply ticket not verified");
    }
}
