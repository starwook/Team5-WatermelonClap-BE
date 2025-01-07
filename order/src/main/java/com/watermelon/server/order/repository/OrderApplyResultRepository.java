package com.watermelon.server.order.repository;

import com.watermelon.server.order.domain.OrderApplyResult;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderApplyResultRepository extends JpaRepository<OrderApplyResult, Long> {


    @Query(value = "SELECT GET_LOCK(:key, :timeoutSeconds)", nativeQuery = true)
    Long getLock(@Param("key") String key, @Param("timeoutSeconds") int timeoutSeconds);

    @Query(value = "SELECT RELEASE_LOCK(:key)", nativeQuery = true)
    Long releaseLock(@Param("key") String key);
}
