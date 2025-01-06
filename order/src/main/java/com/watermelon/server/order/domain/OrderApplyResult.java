package com.watermelon.server.order.domain;


import jakarta.persistence.*;
import lombok.*;



@Getter
@Entity
@RequiredArgsConstructor
@Table(name ="order_result")
public class OrderApplyResult {

    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String applyToken;



    public static OrderApplyResult makeOrderEventApply(String applyToken){
        return OrderApplyResult.builder()
                .applyToken(applyToken)
                .build();
    }

    @Builder
    OrderApplyResult(String applyToken) {
        this.applyToken = applyToken;
    }
}
