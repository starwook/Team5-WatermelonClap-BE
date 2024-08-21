package com.watermelon.server.order.repository;

import com.watermelon.server.order.result.domain.OrderResult;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderResultRepository extends JpaRepository<OrderResult, Long> {


    @Lock(LockModeType.PESSIMISTIC_WRITE) //InnoDb는 레코드 락만 걸기에 테이블락을 걸도록 해야한다
    @Query("select or from OrderResult or")
    List<OrderResult> findAllWithExclusiveLock();

    @Query(value = "SELECT GET_LOCK(:key, :timeoutSeconds)", nativeQuery = true)
    Long getLock(@Param("key") String key, @Param("timeoutSeconds") int timeoutSeconds);

    @Query(value = "SELECT RELEASE_LOCK(:key)", nativeQuery = true)
    Long releaseLock(@Param("key") String key);
}
