package com.watermelon.server.order.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseQuizResultDto {
    private boolean isSuccess;
    public static ResponseQuizResultDto eventTimeOutResult(){
        return new ResponseQuizResultFailDto(false,"이벤트 기간이 아닙니다.");
    }
    public static ResponseQuizResultDto wrongAnswerResult(){
        return new ResponseQuizResultFailDto(false,"이벤트 기간이 아닙니다.");
    }
    public static ResponseQuizResultDto noMoreWinnerResult(){
        return new ResponseQuizResultFailDto(false,"이벤트 기간이 아닙니다.");
    }
    public static ResponseQuizResultDto answerRightResult(String token){
        return new ResponseQuizResultSuccessDto(true,token);
    }
}
