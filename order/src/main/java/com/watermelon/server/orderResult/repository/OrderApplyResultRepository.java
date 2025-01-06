package com.watermelon.server.orderResult.repository;

import com.watermelon.server.orderResult.domain.OrderApplyResult;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderApplyResultRepository extends JpaRepository<OrderApplyResult, Long> {


    @Query(value = "SELECT GET_LOCK(:key, :timeoutSeconds)", nativeQuery = true)
    Long getLock(@Param("key") String key, @Param("timeoutSeconds") int timeoutSeconds);

    @Query(value = "SELECT RELEASE_LOCK(:key)", nativeQuery = true)
    Long releaseLock(@Param("key") String key);
}
