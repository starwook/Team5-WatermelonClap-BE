package com.watermelon.server.event.lottery.domain;

import com.watermelon.server.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Link extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder.Default
    private int viewCount=0;

    private String uri;

    @OneToOne
    private LotteryApplier lotteryApplier;

    public static Link createLink(LotteryApplier lotteryApplier) {

        return Link.builder()
                .lotteryApplier(lotteryApplier)
                .uri(generateLink())
                .build();

    }

    private static String generateLink(){
        return UUID.randomUUID().toString();
    }

    public void addLinkViewCount(){
        viewCount++;
    }

}
