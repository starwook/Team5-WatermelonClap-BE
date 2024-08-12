package com.watermelon.server.event.lottery.repository;

import com.watermelon.server.event.lottery.domain.LotteryEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LotteryEventRepository extends JpaRepository<Long, LotteryEvent> {
}
