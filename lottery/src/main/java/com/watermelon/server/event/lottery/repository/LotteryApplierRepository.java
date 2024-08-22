package com.watermelon.server.event.lottery.repository;

import com.watermelon.server.event.lottery.domain.LotteryApplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LotteryApplierRepository extends JpaRepository<LotteryApplier, Long> {

    List<LotteryApplier> findByLotteryRankNot(int rank);

    List<LotteryApplier> findByLotteryRankNotOOrderByLotteryRank(int rank);

    Optional<LotteryApplier> findByUid(String uid);

    Page<LotteryApplier> findByIsLotteryApplierTrue(Pageable pageable);

    List<LotteryApplier> findByIsLotteryApplierTrue();

    List<LotteryApplier> findByIsPartsWinnerTrue();

    List<LotteryApplier> findByIsPartsApplierTrue();

    boolean existsByUid(String uid);

}
