package com.watermelon.server.order.result.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;


@Getter
@Entity
@RequiredArgsConstructor
public class OrderResult {

    @Id@GeneratedValue
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
