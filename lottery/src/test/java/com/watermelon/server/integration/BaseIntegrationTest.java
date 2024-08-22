package com.watermelon.server.integration;


import com.epages.restdocs.apispec.ResourceSnippet;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.watermelon.server.APITest;
import com.watermelon.server.event.lottery.domain.LotteryApplier;
import com.watermelon.server.event.lottery.domain.LotteryReward;
import com.watermelon.server.event.lottery.service.ExpectationService;
import com.watermelon.server.event.parts.domain.LotteryApplierParts;
import com.watermelon.server.event.parts.domain.Parts;
import com.watermelon.server.event.parts.domain.PartsCategory;
import com.watermelon.server.event.parts.repository.LotteryApplierPartsRepository;
import com.watermelon.server.event.parts.repository.PartsRepository;
import com.watermelon.server.event.lottery.repository.LotteryApplierRepository;
import com.watermelon.server.event.lottery.repository.LotteryRewardRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.watermelon.server.auth.service.TestTokenVerifier.TEST_UID;

@SpringBootTest
@Disabled
@Transactional
public class BaseIntegrationTest extends APITest {

    @Autowired
    protected LotteryApplierRepository lotteryApplierRepository;

    @Autowired
    protected PartsRepository partsRepository;

    @Autowired
    protected LotteryApplierPartsRepository lotteryApplierPartsRepository;
    @Autowired
    private LotteryRewardRepository lotteryRewardRepository;

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

    private LotteryApplier saveTestLotteryWinner(){
        return lotteryApplierRepository.save(LotteryApplier.createTestLotteryWinner(TEST_UID));
    }

    private LotteryApplier saveTestLotteryApplierApplied(){
        LotteryApplier lotteryApplier = LotteryApplier.createLotteryApplier(TEST_UID);
        lotteryApplier.applyLottery();
        return lotteryApplierRepository.save(lotteryApplier);
    }

    private LotteryApplier saveTestLotteryApplier() {
        return lotteryApplierRepository.save(LotteryApplier.createLotteryApplier(TEST_UID));
    }

    private Parts saveTestParts() {
        return saveTestParts(PartsCategory.COLOR);
    }

    private Parts saveTestParts(PartsCategory category) {
        return partsRepository.save(Parts.createTestCategoryParts(category));
    }

    private LotteryApplierParts saveLotteryApplierParts(boolean isEquipped) {
        return saveLotteryApplierParts(isEquipped, saveTestLotteryApplier(), saveTestParts());
    }

    private LotteryApplierParts saveLotteryApplierParts(boolean isEquipped, LotteryApplier lotteryApplier, Parts parts) {
        return lotteryApplierPartsRepository.save(
                LotteryApplierParts.createApplierParts(
                        isEquipped, lotteryApplier, parts));
    }

    private void saveTestLotteryReward() {
        lotteryRewardRepository.save(LotteryReward.createTestLotteryReward());
    }

    @Override
    protected void givenLotteryRewardInfo() {
        saveTestLotteryReward();
    }



    @Override
    protected void givenLotteryWinners() {
        saveTestLotteryWinner();
    }

    @Override
    protected void givenLotteryWinnersForAdmin() {

    }

    @Override
    protected void givenLotteryWinnerInfo() {
        saveTestLotteryWinner();
    }

    @Override
    protected void givenLotteryApplierNotExist() {
        saveTestLotteryApplier();
    }

    @Override
    protected void givenLotteryApplierApplied() {
        saveTestLotteryApplierApplied();
    }

    @Override
    protected void givenLotteryWinner() {
        saveTestLotteryWinner();
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

    @Override
    protected void givenExpectationNotExistForLotteryApplier(String uid) {

    }

    @Override
    protected void givenExpectationAlreadyExistForLotteryApplier(String uid) {

    }

    @Override
    protected void givenLotteryEvent() {
    }
}
