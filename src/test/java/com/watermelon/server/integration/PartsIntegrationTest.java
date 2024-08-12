package com.watermelon.server.integration;

import com.watermelon.server.BaseIntegrationTest;
import com.watermelon.server.event.lottery.auth.service.TestTokenVerifier;
import com.watermelon.server.event.lottery.domain.LotteryApplier;
import com.watermelon.server.event.lottery.parts.domain.LotteryApplierParts;
import com.watermelon.server.event.lottery.parts.domain.Parts;
import com.watermelon.server.event.lottery.parts.domain.PartsCategory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.watermelon.server.Constants.*;
import static com.watermelon.server.common.constants.PathConstants.PARTS;
import static com.watermelon.server.event.lottery.auth.service.TestTokenVerifier.TEST_UID;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("[통합] 파츠 통합 테스트")
public class PartsIntegrationTest extends BaseIntegrationTest {


    @Test
    @DisplayName("파츠 뽑기 결과 반환 - 성공")
    void drawParts() throws Exception {

        givenLotteryApplierWhoDrawsPartsFirst();

        whenDrawParts();

        thenDrawParts();

    }

    @Test
    @DisplayName("파츠 뽑기 결과 반환 - 실패 (횟수 소진)")
    void drawPartsException() throws Exception {

        givenLotteryApplierWhoHasNoRemainChance();

        whenDrawParts();

        thenDrawPartsHasNotRemainChance();

    }

    @Test
    @DisplayName("파츠 상태 변경 - 성공")
    void toggleParts() throws Exception {

        givenPartsNotEquipped();

        whenPartsEquippedStatusIsChanged(
                lotteryApplierPartsRepository.findLotteryApplierPartsByLotteryApplierUid(TEST_UID)
                        .get(0).getParts().getId()
        );

        thenPartsEquippedStatusIsChanged();

    }

    @Test
    @DisplayName("파츠 뽑기 횟수 반환 - 성공")
    void getRemainChance() throws Exception {

        givenLotteryApplierWhoHasRemainChance();

        whenMyRemainChanceIsRetrieved();

        thenMyRemainChanceIsRetrieved();

    }

    @Test
    @DisplayName("파츠 목록 반환 - 성공")
    void getMyPartsList() throws Exception {

        givenMyPartsList();

        whenMyPartsListAreRetrieved();

        thenMyPartsListAreRetrieved();

    }

    @Test
    @DisplayName("Uri 주인의 파츠 목록 반환 - 성공")
    void getLinkPartsList() throws Exception {

        givenPartsListForUri();

        whenPartsListAreRetrievedWithUri(
                lotteryApplierRepository.findByUid(TEST_UID).get().getLink().getUri()
        );

        thenPartsListAreRetrievedWithUri();

    }


    //@Test
    //@DisplayName("추첨권 부여 - 성공")
    void partsLotteryPolicyTest() throws Exception {

        //given
        LotteryApplier lotteryApplier = saveTestLotteryApplier();
        for(PartsCategory partsCategory:PartsCategory.values()){
            Parts parts = partsRepository.save(Parts.createTestCategoryParts(partsCategory));
            lotteryApplierPartsRepository.save(
                    LotteryApplierParts.createApplierParts(true, lotteryApplier, parts)
            );
        }

        //when
        postPartsLotteryRequestTestUser();
        boolean expected = findTestLotteryApplier().isPartsApplier();

        //then
        Assertions.assertThat(expected).isTrue();

    }

    @Test
    @DisplayName("파츠 뽑기 - 실패 (뽑기권 없음)")
    void partsLotteryNotRemainCountExceptionTest() throws Exception {

        //given
        postPartsLotteryRequestTestUser();

        //when
        mvc.perform(post(PARTS)
                .header( HEADER_NAME_AUTHORIZATION, HEADER_VALUE_BEARER+HEADER_VALUE_SPACE+TestTokenVerifier.TEST_VALID_TOKEN)
        )
                //then
                .andExpect(status().isTooManyRequests());

    }

    private LotteryApplier saveTestLotteryApplier() {
        LotteryApplier lotteryApplier = LotteryApplier.createLotteryApplier(TEST_UID);
        lotteryApplierRepository.save(lotteryApplier);
        return lotteryApplier;
    }

    private LotteryApplier findTestLotteryApplier() {
        final String firstUserUid = TEST_UID;
        return lotteryApplierRepository.findByUid(firstUserUid).orElseThrow();
    }

    private void postPartsLotteryRequestTestUser() throws Exception {
        final String firstUserToken = TestTokenVerifier.TEST_VALID_TOKEN;
        mvc.perform(post(PARTS).header(
                HEADER_NAME_AUTHORIZATION, HEADER_VALUE_BEARER+HEADER_VALUE_SPACE+ firstUserToken
        ));
    }

}
