package com.watermelon.server.order.result.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;



@Getter
@Entity
@RequiredArgsConstructor
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
