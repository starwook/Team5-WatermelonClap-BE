package com.watermelon.server;


import com.epages.restdocs.apispec.ResourceSnippet;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.watermelon.server.event.lottery.domain.LotteryApplier;
import com.watermelon.server.event.lottery.parts.domain.LotteryApplierParts;
import com.watermelon.server.event.lottery.parts.domain.Parts;
import com.watermelon.server.event.lottery.parts.domain.PartsCategory;
import com.watermelon.server.event.lottery.parts.repository.LotteryApplierPartsRepository;
import com.watermelon.server.event.lottery.parts.repository.PartsRepository;
import com.watermelon.server.event.lottery.repository.LotteryApplierRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.watermelon.server.event.lottery.auth.service.TestTokenVerifier.TEST_UID;

@SpringBootTest
@Disabled
@Transactional
@Import(PartsRegistrationConfig.class)
public class BaseIntegrationTest extends APITest {

    @Autowired
    protected LotteryApplierRepository lotteryApplierRepository;

    @Autowired
    protected PartsRepository partsRepository;

    @Autowired
    protected LotteryApplierPartsRepository lotteryApplierPartsRepository;

    protected ResourceSnippet resourceSnippet(String description) {
        return resource(
                ResourceSnippetParameters.builder()
                        .description(description)
                        .build()
        );
    }

    protected ResourceSnippet resourceSnippetAuthed(String description) {

        return resource(
                ResourceSnippetParameters.builder()
                        .description(description)
                        .requestHeaders(
                                headerWithName("Authorization").description("Bearer token for authentication"))
                        .build()
        );

    }

    private LotteryApplier saveTestLotteryApplier() {
        return lotteryApplierRepository.save(LotteryApplier.createLotteryApplier(TEST_UID));
    }

    private Parts saveTestParts() {
        return partsRepository.save(Parts.createTestCategoryParts(PartsCategory.COLOR));
    }

    private LotteryApplierParts saveLotteryApplierParts(boolean isEquipped) {
        return lotteryApplierPartsRepository.save(
                LotteryApplierParts.createApplierParts(
                        isEquipped, saveTestLotteryApplier(), saveTestParts())
        );
    }

    @Override
    protected void givenLotteryRewardInfo() {

    }

    @Override
    protected void givenLotteryWinners() {

    }

    @Override
    protected void givenLotteryWinnersForAdmin() {

    }

    @Override
    protected void givenLotteryWinnerInfo() {

    }

    @Override
    protected void givenLotteryApplierNotExist() {

    }

    @Override
    protected void givenLotteryWinner() {

    }

    @Override
    protected void givenPartsListForUri() {
        saveLotteryApplierParts(true);
    }

    @Override
    protected void givenPartsNotEquipped() {
        saveLotteryApplierParts(false);
    }

    @Override
    protected void givenLotteryApplierWhoHasNoRemainChance() {
        LotteryApplier lotteryApplier = LotteryApplier.createLotteryApplier(TEST_UID);
        while (lotteryApplier.getRemainChance() > 0) {
            lotteryApplier.drawParts();
        }
        Assertions.assertThat(lotteryApplier.getRemainChance()).isZero();
        lotteryApplierRepository.save(lotteryApplier);
    }

    @Override
    protected void givenLotteryApplierWhoDrawsPartsFirst() {

    }

    @Override
    protected void givenLotteryApplierWhoHasRemainChance() {

    }

    @Override
    protected void givenMyPartsList() {
        saveLotteryApplierParts(true);
    }

    @Override
    protected void givenLink() {

    }

    @Override
    protected void givenOriginUri() {

    }
}
