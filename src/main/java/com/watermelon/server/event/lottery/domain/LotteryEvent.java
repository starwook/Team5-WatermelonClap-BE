package com.watermelon.server.event.lottery.domain;

import com.watermelon.server.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class LotteryEvent extends BaseEntity {

    @Id
    private Long id;

    private String name;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @OneToMany(mappedBy = "lotteryEvent")
    private List<LotteryReward> rewards;

}
