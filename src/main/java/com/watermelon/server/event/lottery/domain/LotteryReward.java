package com.watermelon.server.event.lottery.domain;

import com.watermelon.server.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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

    public static LotteryReward createTestLotteryReward(){

        LotteryReward lotteryReward = new LotteryReward();
        lotteryReward.lotteryRank = 1;
        lotteryReward.imgSrc = "test.png";
        lotteryReward.name = "test";
        lotteryReward.winnerCount = 1;
        return lotteryReward;

    }

}
