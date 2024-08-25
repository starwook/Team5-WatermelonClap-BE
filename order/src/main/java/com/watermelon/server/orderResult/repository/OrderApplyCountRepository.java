package com.watermelon.server.orderResult.repository;

import com.watermelon.server.orderResult.domain.OrderApplyCount;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderApplyCountRepository extends JpaRepository<OrderApplyCount,Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE) //InnoDb는 레코드락만 건다
    @Query("select oac from OrderApplyCount oac order by oac.id asc limit 1")
    Optional<OrderApplyCount> findWithExclusiveLock();
    @Query("select oac from OrderApplyCount oac order by oac.id asc limit 1")
    Optional<OrderApplyCount> findCurrent();

}
