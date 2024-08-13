package com.watermelon.server.event.lottery.parts.repository;

import com.watermelon.server.event.lottery.domain.LotteryApplier;
import com.watermelon.server.event.lottery.parts.domain.LotteryApplierParts;
import com.watermelon.server.event.lottery.parts.domain.PartsCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LotteryApplierPartsRepository extends JpaRepository<LotteryApplierParts, Long> {

    List<LotteryApplierParts> findLotteryApplierPartsByLotteryApplierId(Long lotteryApplierId);
    List<LotteryApplierParts> findLotteryApplierPartsByLotteryApplierUid(String lotteryApplierUid);

    Optional<LotteryApplierParts> findLotteryApplierPartsByLotteryApplierUidAndPartsId(String lotteryApplierUid,Long partsId);

    @Query("SELECT COUNT(DISTINCT lap.parts.category) FROM LotteryApplierParts lap WHERE lap.lotteryApplier = :lotteryApplier")
    long countDistinctPartsCategoryByLotteryApplier(@Param("lotteryApplier") LotteryApplier lotteryApplier);

    @Query("SELECT lap FROM LotteryApplierParts lap WHERE lap.parts.category = :category AND lap.lotteryApplier.uid = :lotteryApplierUid AND lap.isEquipped = true")
    Optional<LotteryApplierParts> findEquippedLotteryApplierPartsByPartsCategoryAndLotteryApplier(
            @Param("lotteryApplierUid") String lotteryApplierUid,
            @Param("category") PartsCategory partsCategory
    );

}