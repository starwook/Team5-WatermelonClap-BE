package com.watermelon.server.event.lottery.error;

public class ExpectationAlreadyExistError extends Exception{
    public ExpectationAlreadyExistError() {
        super("expectation_already_exist");
    }
}
