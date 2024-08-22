package com.watermelon.server.event.parts.repository;

import com.watermelon.server.event.parts.domain.Parts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PartsRepository extends JpaRepository<Parts, Long> {

    @Query("select count(distinct parts.category) from Parts parts")
    long countCategoryDistinct();

    @Query("update LotteryApplier l set l.isPartsWinner = false")
    @Modifying
    void updateAllIsPartsWinnerFalse();

}
