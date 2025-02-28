package com.watermelon.server.order.domain;


import com.watermelon.server.BaseEntity;
import com.watermelon.server.order.dto.request.OrderEventWinnerRequestDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
@Getter
@Table(name = "order_event_winner")
public class OrderEventWinner extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String applyAnswer;
    private String applyTicket;
    private String phoneNumber;
    @ManyToOne
    @JoinColumn
    private OrderEvent orderEvent;

    @Builder
    public OrderEventWinner(String phoneNumber,OrderEvent orderEvent,String applyAnswer,String applyTicket) {
        this.phoneNumber = phoneNumber;
        this.orderEvent = orderEvent;
        this.applyAnswer = applyAnswer;
        this.applyTicket = applyTicket;
    }
    public static OrderEventWinner makeWinner(OrderEvent orderEvent
            , OrderEventWinnerRequestDto orderEventWinnerRequestDto
    ,String applyAnswer,String applyTicket) {
        return OrderEventWinner.builder()
                .phoneNumber(orderEventWinnerRequestDto.getPhoneNumber())
                .orderEvent(orderEvent)
                .applyAnswer(applyAnswer)
                .applyTicket(applyTicket)
                .build();
    }
}
