package com.watermelon.server.fifoevent.dto.response;


import com.watermelon.server.fifoevent.domain.Quiz;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ResponseQuizDto {
    private Long quizId;
    private String description;
    private String imgSrc;
    private String title;


    public static ResponseQuizDto from(Quiz quiz) {
        return ResponseQuizDto.builder()
                .quizId(quiz.getId())
                .description(quiz.getDescription())
                .imgSrc(quiz.getImgSrc())
                .title(quiz.getTitle())
                .build();
    }
}
