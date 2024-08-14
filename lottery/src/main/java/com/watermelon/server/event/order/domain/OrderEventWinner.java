package com.watermelon.server.event.order.domain;


import com.watermelon.server.BaseEntity;
import com.watermelon.server.event.order.dto.request.OrderEventWinnerRequestDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
@Getter
public class OrderEventWinner extends BaseEntity {
    @Id @GeneratedValue
    private Long id;

    private String applyAnswer;
    private String applyToken;
    private String phoneNumber;
    @ManyToOne
    @JoinColumn
    private OrderEvent orderEvent;

    @Builder
    public OrderEventWinner(String phoneNumber,OrderEvent orderEvent,String applyAnswer,String applyToken) {
        this.phoneNumber = phoneNumber;
        this.orderEvent = orderEvent;
        this.applyAnswer = applyAnswer;
        this.applyToken = applyToken;
    }
    public static OrderEventWinner makeWinner(OrderEvent orderEvent
            , OrderEventWinnerRequestDto orderEventWinnerRequestDto
    ,String applyAnswer,String applyTicket) {
        return OrderEventWinner.builder()
                .phoneNumber(orderEventWinnerRequestDto.getPhoneNumber())
                .orderEvent(orderEvent)
                .applyAnswer(applyAnswer)
                .applyToken(applyTicket)
                .build();
    }


}
