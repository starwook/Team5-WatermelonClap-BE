package com.watermelon.server.event.parts.repository;

import com.watermelon.server.event.parts.domain.PartsReward;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartsRewardRepository extends JpaRepository<PartsReward, Long> {
}
