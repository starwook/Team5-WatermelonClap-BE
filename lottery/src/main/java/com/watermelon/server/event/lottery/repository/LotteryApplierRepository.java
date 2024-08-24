package com.watermelon.server.event.lottery.repository;

import com.watermelon.server.event.lottery.domain.LotteryApplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LotteryApplierRepository extends JpaRepository<LotteryApplier, Long> {

    List<LotteryApplier> findByLotteryRankNot(int rank);

    List<LotteryApplier> findByLotteryRankNotOrderByLotteryRank(int rank);

    @Query("SELECT l FROM LotteryApplier l WHERE l.link.uri = :linkUri")
    LotteryApplier findByLotteryApplierByLinkUri(@Param("linkUri") String linkUri);

    @Query("UPDATE LotteryApplier l SET l.lotteryRank = :rank")
    @Modifying
    void initAllLotteryRank(@Param("rank") int rank);

    Optional<LotteryApplier> findByUid(String uid);

    Page<LotteryApplier> findByIsLotteryApplierTrue(Pageable pageable);

    List<LotteryApplier> findByIsLotteryApplierTrue();

    List<LotteryApplier> findByIsPartsWinnerTrue();

    List<LotteryApplier> findByIsPartsApplierTrue();

    boolean existsByUid(String uid);

}
