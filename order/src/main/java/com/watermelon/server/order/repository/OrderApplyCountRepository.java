package com.watermelon.server.order.repository;

import com.watermelon.server.order.domain.OrderWinningCount;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderApplyCountRepository extends JpaRepository<OrderWinningCount,Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE) //InnoDb는 레코드락만 건다
    @QueryHints({
            @QueryHint(name = "jakarta.persistence.lock.timeout", value = "2000")
    })
    @Query("select oac from OrderWinningCount oac order by oac.id asc limit 1")
    Optional<OrderWinningCount> findLimitOneExclusiveLock();


    @Lock(LockModeType.PESSIMISTIC_WRITE) //InnoDb는 레코드락만 건다
    @QueryHints({
            @QueryHint(name = "jakarta.persistence.lock.timeout", value = "2000")
    })
    @Query("select oac from OrderWinningCount oac where oac.id = :id")
    Optional<OrderWinningCount> findWithIdExclusiveLock(@Param("id") Long id);

    @Query("select oac from OrderWinningCount oac order by oac.id asc limit 1")
    Optional<OrderWinningCount> findFirstApplyCountById();


    /**
     *
     mysql 네임드락 쿼리
     */
    @Query(value = "SELECT GET_LOCK(:lockName, :timeout)", nativeQuery = true)
    Optional<Integer> acquireNamedLock(@Param("lockName") String lockName, @Param("timeout") int timeout);

    @Query(value = "SELECT RELEASE_LOCK(:lockName)", nativeQuery = true)
    Optional<Integer> releaseNamedLock(@Param("lockName") String lockName);
}
