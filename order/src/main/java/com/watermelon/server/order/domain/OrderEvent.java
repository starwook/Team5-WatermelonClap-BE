package com.watermelon.server.order.domain;

import com.watermelon.server.BaseEntity;
import com.watermelon.server.order.dto.request.RequestOrderEventDto;
import jakarta.persistence.*;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "order_event")
@RequiredArgsConstructor
public class OrderEvent extends BaseEntity {

    private static final Logger log = LoggerFactory.getLogger(OrderEvent.class);
    @Id @GeneratedValue
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    private OrderEventReward orderEventReward;

    @OneToOne(cascade = CascadeType.ALL)
    private Quiz quiz;

    @OneToOne(cascade = CascadeType.ALL)
    private OrderWinningCount orderWinningCount;

    @OneToMany(mappedBy = "orderEvent")
    private List<OrderEventWinner> orderEventWinner = new ArrayList<>();
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int winnerCount;

    @Setter
//    @Enumerated(EnumType.STRING)
    private OrderEventStatus orderEventStatus;

    public OrderEvent(Quiz quiz, LocalDateTime startDate, LocalDateTime endDate) {
        this.quiz = quiz;
        this.startDate = startDate;
        this.endDate = endDate;
    }
    @Builder
    OrderEvent(int winnerCount, LocalDateTime startDate, LocalDateTime endDate, Quiz quiz, OrderEventReward orderEventReward,Long id){
        this.id = id;
        this.winnerCount = winnerCount;
        this.endDate = endDate;
        this.startDate = startDate;
        this.quiz = quiz;
        this.orderEventReward = orderEventReward;
        this.orderEventStatus = OrderEventStatus.UPCOMING;

    }

    public static OrderEvent makeOrderEventWithOutImage(RequestOrderEventDto requestOrderEventDto){
        Quiz quiz = Quiz.makeQuiz(requestOrderEventDto.getQuiz());
        OrderEventReward reward = OrderEventReward.makeReward(requestOrderEventDto.getReward());
        return OrderEvent.builder()
                .quiz(quiz)
                .startDate(requestOrderEventDto.getStartDate())
                .endDate(requestOrderEventDto.getEndDate())
                .orderEventReward(reward)
                .winnerCount(requestOrderEventDto.getWinnerCount())
                .build();
    }
    public static OrderEvent makeOrderEventWithOutImageNewApplyCount(RequestOrderEventDto requestOrderEventDto){
        Quiz quiz = Quiz.makeQuiz(requestOrderEventDto.getQuiz());
        OrderEventReward reward = OrderEventReward.makeReward(requestOrderEventDto.getReward());
        return OrderEvent.builder()
                .quiz(quiz)
                .startDate(requestOrderEventDto.getStartDate())
                .endDate(requestOrderEventDto.getEndDate())
                .orderEventReward(reward)
                .winnerCount(requestOrderEventDto.getWinnerCount())
//                .orderApplyCount(OrderApplyCount.createWithNothing())
                .build();
    }
    public static OrderEvent makeOrderEventWithImage(RequestOrderEventDto requestOrderEventDto,String rewardImgSrc,String quizImgSrc){
        Quiz quiz = Quiz.makeQuizWithImage(requestOrderEventDto.getQuiz(),quizImgSrc);
        OrderEventReward reward = OrderEventReward.makeRewardWithImage(requestOrderEventDto.getReward(),rewardImgSrc);
        return OrderEvent.builder()
                .quiz(quiz)
                .startDate(requestOrderEventDto.getStartDate())
                .endDate(requestOrderEventDto.getEndDate())
                .orderEventReward(reward)
                .winnerCount(requestOrderEventDto.getWinnerCount())
                .build();
    }
    public static OrderEvent makeOrderEventWithImageNewApplyCount(RequestOrderEventDto requestOrderEventDto,String rewardImgSrc,String quizImgSrc){
        Quiz quiz = Quiz.makeQuizWithImage(requestOrderEventDto.getQuiz(),quizImgSrc);
        OrderEventReward reward = OrderEventReward.makeRewardWithImage(requestOrderEventDto.getReward(),rewardImgSrc);
        return OrderEvent.builder()
                .quiz(quiz)
                .startDate(requestOrderEventDto.getStartDate())
                .endDate(requestOrderEventDto.getEndDate())
                .orderEventReward(reward)
                .winnerCount(requestOrderEventDto.getWinnerCount())
//                .orderApplyCount(OrderApplyCount.createWithNothing())
                .build();
    }

    public static OrderEvent makeOrderEventWithInputIdForDocumentation(RequestOrderEventDto requestOrderEventDto, Long id){
        Quiz quiz = Quiz.makeQuizInputId(requestOrderEventDto.getQuiz(),id);
        OrderEventReward reward = OrderEventReward.makeRewardInputId(requestOrderEventDto.getReward(),id);
        return OrderEvent.builder()
                .id(id)
                .quiz(quiz)
                .startDate(requestOrderEventDto.getStartDate())
                .endDate(requestOrderEventDto.getEndDate())
                .orderEventReward(reward)
                .winnerCount(requestOrderEventDto.getWinnerCount())
                .build();
    }


    @Transactional
    public void changeOrderEventStatusByTime(LocalDateTime now){
        if(orderEventStatus.equals(OrderEventStatus.END)) return;
        if(orderEventStatus.equals(OrderEventStatus.UPCOMING)){
            if(now.isAfter(startDate)) {
                changeOrderEventStatus(OrderEventStatus.OPEN);
                log.info("EVENT OPEN");
            }
        }
        if(orderEventStatus.equals(OrderEventStatus.OPEN)||orderEventStatus.equals(OrderEventStatus.CLOSED)){
            if(now.isAfter(endDate)){
                changeOrderEventStatus(OrderEventStatus.END);
                log.info("EVENT END");
            }
        }
    }


    public void changeOrderEventStatus(OrderEventStatus orderEventStatus){
        this.orderEventStatus = orderEventStatus;
    }

    public boolean isTimeInEventTime(LocalDateTime time){
        if(time.isAfter(startDate)&&time.isBefore(endDate)){ return true;}
        return false;
    }
    public void openEvent(){
        this.orderEventStatus = OrderEventStatus.OPEN;
    }
    @Override
    public String toString() {
        return "OrderEvent{" +
                "id=" + id +
                ", orderEventReward=" + orderEventReward +
                ", quiz=" + quiz +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", winnerCount=" + winnerCount +
                ", orderEventStatus=" + orderEventStatus +
                '}';
    }
}
