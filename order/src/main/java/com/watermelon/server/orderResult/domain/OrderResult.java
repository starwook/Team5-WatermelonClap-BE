package com.watermelon.server.orderResult.domain;


import jakarta.persistence.*;
import lombok.*;



@Getter
@Entity
@RequiredArgsConstructor
@Table(name ="order_result")
public class OrderResult {

    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String applyToken;



    public static OrderResult makeOrderEventApply(String applyToken){
        return OrderResult.builder()
                .applyToken(applyToken)
                .build();
    }

    @Builder
    OrderResult(String applyToken) {
        this.applyToken = applyToken;
    }
}
