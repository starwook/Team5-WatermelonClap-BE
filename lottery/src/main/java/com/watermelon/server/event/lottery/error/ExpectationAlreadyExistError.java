package com.watermelon.server.event.lottery.error;

public class ExpectationAlreadyExistError extends Exception{
    public ExpectationAlreadyExistError() {
        super("해당 유저의 기대평이 이미 존재합니다.");
    }
}
