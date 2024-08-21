package com.watermelon.server.event.lottery.dto.response;

import lombok.Data;

@Data
public class ResponseExpectationCheckDto {

    private boolean isExist;

    private ResponseExpectationCheckDto(boolean isExist) {
        this.isExist = isExist;
    }

    public static ResponseExpectationCheckDto from(boolean isExist) {
        return new ResponseExpectationCheckDto(isExist);
    }

}
