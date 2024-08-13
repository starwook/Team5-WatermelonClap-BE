package com.watermelon.server.error;



public class ApplyTicketWrongException extends Exception{
    public ApplyTicketWrongException(){
        super("apply ticket not verified");
    }
}
