package com.watermelon.server.event.lottery.domain;

import com.watermelon.server.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
public class LotteryEvent extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    private String name;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @Cascade(CascadeType.ALL)
    @OneToMany(mappedBy = "lotteryEvent")
    private List<LotteryReward> rewards;

    public static LotteryEvent create(String name, LocalDateTime startTime, LocalDateTime endTime, List<LotteryReward> rewards) {
        LotteryEvent event = new LotteryEvent();
        event.name = name;
        event.startTime = startTime;
        event.endTime = endTime;
        event.rewards = rewards;
        return event;
    }

    public static LotteryEvent createTest(){

        return LotteryEvent.create(
                "test",
                LocalDateTime.now(),
                LocalDateTime.now(),
                null
        );

    }

}
