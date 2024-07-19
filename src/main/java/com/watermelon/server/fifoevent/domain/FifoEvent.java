package com.watermelon.server.fifoevent.domain;

import com.watermelon.server.BaseEntity;
import com.watermelon.server.fifoevent.dto.request.RequestFiFoEventDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@RequiredArgsConstructor
public class FifoEvent extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    @OneToOne
    private FifoReward fifoReward;

    @OneToOne(cascade = CascadeType.ALL)
    private Quiz quiz;

    @OneToMany(mappedBy = "fifoEvent")
    private List<FifoWinner> fifoWinner = new ArrayList<>();
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int maxWinnerCount;
    private int winnerCount;

    public boolean isWinnerAddable(){
        if(winnerCount<maxWinnerCount) return true;
        return false;
    }

    public static FifoEvent makeFifoEvent(RequestFiFoEventDto requestFiFoEventDto){
        Quiz quiz = Quiz.makeQuiz(requestFiFoEventDto.getQuestion(), requestFiFoEventDto.getAnswer());
        return new FifoEvent(requestFiFoEventDto.getMaxWinnerCount(),requestFiFoEventDto.getStartTime(),requestFiFoEventDto.getEndTime(),quiz);
    }
    public boolean isTimeInEventTime(LocalDateTime time){
        if(time.isAfter(startTime)&&time.isBefore(endTime)){ return true;}
        return false;
    }
    FifoEvent(int maxWinnerCount,LocalDateTime startTime,LocalDateTime endTime,Quiz quiz){
        this.maxWinnerCount = maxWinnerCount;
        this.endTime = endTime;
        this.startTime = startTime;
        this.quiz = quiz;
    }
}
