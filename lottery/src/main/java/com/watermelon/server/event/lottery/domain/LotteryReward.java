package com.watermelon.server.event.lottery.domain;

import com.watermelon.server.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class LotteryReward extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    private int lotteryRank;

    private String imgSrc;

    private String name;

    private int winnerCount;

    @ManyToOne
    @JoinColumn
    private LotteryEvent lotteryEvent;

    public static LotteryReward createLotteryReward(int lotteryRank, String imgSrc, String name, int winnerCount) {
        LotteryReward lotteryReward = new LotteryReward();
        lotteryReward.lotteryRank = lotteryRank;
        lotteryReward.imgSrc = imgSrc;
        lotteryReward.name = name;
        lotteryReward.winnerCount = winnerCount;
        return lotteryReward;
    }

    public static LotteryReward createTestLotteryReward(){

        LotteryReward lotteryReward = new LotteryReward();
        lotteryReward.lotteryRank = 1;
        lotteryReward.imgSrc = "test.png";
        lotteryReward.name = "test";
        lotteryReward.winnerCount = 1;
        return lotteryReward;

    }

}
