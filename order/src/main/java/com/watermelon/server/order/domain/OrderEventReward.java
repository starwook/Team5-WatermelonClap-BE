package com.watermelon.server.order.domain;


import com.watermelon.server.order.dto.request.RequestOrderRewardDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "order_event_reward")
public class OrderEventReward {

    @Id @GeneratedValue
    private Long id;

    @OneToOne
    private OrderEvent orderEvent;

    private String name;

    private String imgSrc;



    public static OrderEventReward makeReward(RequestOrderRewardDto requestOrderRewardDto){
        return OrderEventReward.builder()
                .name(requestOrderRewardDto.getName())
                .build();
    }
    public static OrderEventReward makeRewardInputId(RequestOrderRewardDto requestOrderRewardDto,Long id){
        return OrderEventReward.builder()
                .id(id)
                .name(requestOrderRewardDto.getName())
                .build();
    }

    public static OrderEventReward makeRewardWithImage(RequestOrderRewardDto requestOrderRewardDto, String imgSrc){
        return OrderEventReward.builder()
                .name(requestOrderRewardDto.getName())
                .imgSrc(imgSrc)
                .build();
    }
    @Builder
    public OrderEventReward(Long id, String name, String imgSrc) {
        this.name = name;
        this.id = id;
        this.imgSrc = imgSrc;
    }
}
