package com.watermelon.server.event.parts.repository;

import com.watermelon.server.event.lottery.domain.LotteryApplier;
import com.watermelon.server.event.parts.domain.LotteryApplierParts;
import com.watermelon.server.event.parts.domain.Parts;
import com.watermelon.server.event.parts.domain.PartsCategory;
import com.watermelon.server.event.lottery.repository.LotteryApplierRepository;
import com.watermelon.server.event.parts.repository.LotteryApplierPartsRepository;
import com.watermelon.server.event.parts.repository.PartsRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DisplayName("[단위] 응모자-파츠 레포지토리")
@DataJpaTest
class LotteryApplierPartsRepositoryTest {

    @Autowired
    private LotteryApplierPartsRepository lotteryApplierPartsRepository;

    @Autowired
    private PartsRepository partsRepository;

    @Autowired
    private LotteryApplierRepository lotteryApplierRepository;

    @Test
    @DisplayName("응모자의 파츠 수 조회 - 성공")
    void countDistinctPartsCategoryByLotteryApplier() {

        //given
        LotteryApplier lotteryApplier = Mockito.mock(LotteryApplier.class);
        lotteryApplierRepository.save(lotteryApplier);

        saveLotteryApplierAllParts(lotteryApplier);
        saveLotteryApplierAllParts(lotteryApplier);

        //when
        long actual = lotteryApplierPartsRepository.countDistinctPartsCategoryByLotteryApplier(lotteryApplier);

        //then
        Assertions.assertThat(actual).isEqualTo(PartsCategory.values().length);

    }

    private void saveLotteryApplierAllParts(LotteryApplier lotteryApplier) {
        for (PartsCategory category : PartsCategory.values()) {
            Parts parts = Parts.createTestCategoryParts(category);
            partsRepository.save(parts);
            lotteryApplierPartsRepository.save(
                    LotteryApplierParts.createApplierParts(
                            false,
                            lotteryApplier,
                            parts
                    )
            );
        }
    }

}