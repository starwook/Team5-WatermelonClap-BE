package com.watermelon.server.order.repository;

import com.watermelon.server.order.result.domain.OrderApplyCount;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderApplyCountRepository extends JpaRepository<OrderApplyCount,Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE) //InnoDb는 레코드락만 건다
    @Query("select oac from OrderApplyCount oac")
    Optional<OrderApplyCount> findWithExclusiveLock();
    @Query("select oac from OrderApplyCount oac")
    Optional<OrderApplyCount> findCurrent();
}
