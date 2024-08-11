package com.watermelon.server.integration;

import com.watermelon.server.BaseIntegrationTest;
import com.watermelon.server.event.lottery.auth.service.TestTokenVerifier;
import com.watermelon.server.event.lottery.domain.LotteryApplier;
import com.watermelon.server.event.lottery.parts.domain.Parts;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.watermelon.server.Constants.*;
import static com.watermelon.server.common.constants.PathConstants.PARTS;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@DisplayName("[통합] 추첨 통합 테스트")
public class LotteryIntegrationTest extends BaseIntegrationTest {

    @BeforeEach
    void setUp() {
        partsRepository.saveAll(Parts.createAllParts());
    }

    @Test
    @DisplayName("당첨자 명단 반환 - 성공")
    void testGetOrderEventResultSuccess() throws Exception {

        givenLotteryWinners();

        whenLotteryWinnersAreRetrieved();

        thenLotteryWinnersAreRetrieved();

    }

    @Test
    @DisplayName("당첨자 정보 반환 - 성공")
    void testGetOrderEventResultFailure() throws Exception {

        givenLotteryWinnerInfo();

        whenLotteryWinnerInfoIsRetrieved();

        thenLotteryWinnerInfoIsRetrieved();

    }

    @Test
    @DisplayName("당첨자 정보 저장 - 성공")
    void testCreateLotteryWinnerInfoSuccess() throws Exception {

        whenLotteryWinnerInfoIsAdded();

        thenLotteryWinnerInfoIsAdded();

    }

    @Test
    @DisplayName("응모 정보 반환 - 실패 (정보 없음)")
    void testGetLotteryRankNotAppliedCase() throws Exception {

        givenLotteryApplierNotExist();

        whenLotteryAppliersRankIsRetrieved();

        thenLotteryAppliersRankIsRetrievedWithNoInfo();

    }

    @Test
    @DisplayName("응모 정보 반환 - 성공 (응모했는데 당첨 됨)")
    void testGetLotteryRankAppliedCase() throws Exception {

        givenLotteryWinner();

        whenLotteryAppliersRankIsRetrieved();

        thenLotteryAppliersRankIsRetrievedForWinner();

    }

    @Test
    @DisplayName("응모 정보 반환 - 성공 (응모했는데 당첨 안됨)")
    void testGetLotteryRankAppliedButNotWinnerCase() throws Exception {

        givenLotteryApplierApplied();

        whenLotteryAppliersRankIsRetrieved();

        thenLotteryAppliersRankIsRetrievedForApplier();

    }

    @Test
    @DisplayName("추첨이벤트 경품 정보 반환 - 성공")
    void getRewardInfo() throws Exception {

        givenLotteryRewardInfo();

        whenLotteryRewardInfoIsRetrieved();

        thenLotteryRewardInfoIsRetrieved();

    }

    @Test
    @DisplayName("추첨 이벤트 응모 - 성공")
    void applySuccessTest() throws Exception {

        //when
        postPartsLotteryRequestTestUser();

        //then
        LotteryApplier lotteryApplier = findTestLotteryApplier();
        Assertions.assertThat(lotteryApplier.isLotteryApplier()).isTrue();
    }

    private LotteryApplier findTestLotteryApplier() {
        final String firstUserUid = TestTokenVerifier.TEST_UID;
        return lotteryApplierRepository.findByUid(firstUserUid).orElseThrow();
    }

    private void postPartsLotteryRequestTestUser() throws Exception {
        final String firstUserToken = TestTokenVerifier.TEST_VALID_TOKEN;
        mvc.perform(post(PARTS).header(
                HEADER_NAME_AUTHORIZATION, HEADER_VALUE_BEARER+HEADER_VALUE_SPACE+ firstUserToken
        ));
    }

}
