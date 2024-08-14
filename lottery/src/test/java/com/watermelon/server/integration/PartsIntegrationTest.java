package com.watermelon.server.integration;

import com.watermelon.server.auth.service.TestTokenVerifier;
import com.watermelon.server.event.lottery.domain.LotteryApplier;
import com.watermelon.server.event.parts.domain.LotteryApplierParts;
import com.watermelon.server.event.parts.domain.Parts;
import com.watermelon.server.event.parts.domain.PartsCategory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.watermelon.server.constants.Constants.*;
import static com.watermelon.server.common.constants.PathConstants.PARTS;
import static com.watermelon.server.auth.service.TestTokenVerifier.TEST_UID;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("[통합] 파츠 통합 테스트")
public class PartsIntegrationTest extends BaseIntegrationTest {

    @BeforeEach
    void setUp(){
        partsRepository.saveAll(
                Parts.createAllParts()
        );
    }

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

    @Test
    @DisplayName("파츠 장착 - 파츠를 장착하면 해당 카테고리의 다른 파츠 장착 해제되어야 함")
    void partsLotteryPartsEquippedTest() throws Exception {

        //given
        LotteryApplier lotteryApplier = saveTestLotteryApplier();

        Parts parts1 = partsRepository.save(
                Parts.builder()
                        .category(PartsCategory.WHEEL)
                        .build()
        );

        Parts parts2 = partsRepository.save(
                Parts.builder()
                        .category(PartsCategory.WHEEL)
                        .build()
        );

        LotteryApplierParts lotteryApplierParts1 = lotteryApplierPartsRepository.save(
                LotteryApplierParts.createApplierParts(true, lotteryApplier, parts1)
        );

        LotteryApplierParts lotteryApplierParts2 = lotteryApplierPartsRepository.save(
                LotteryApplierParts.createApplierParts(false, lotteryApplier, parts2)
        );

        //when
        whenPartsEquippedStatusIsChanged(parts2.getId());

        //then
        Assertions.assertThat(lotteryApplierParts1.isEquipped()).isFalse();
        Assertions.assertThat(lotteryApplierParts2.isEquipped()).isTrue();

    }

    @Test
    @DisplayName("파츠 장착 - 해제 케이스")
    void partsReleaseTest() throws Exception {

        //given
        LotteryApplier lotteryApplier = saveTestLotteryApplier();

        Parts parts1 = partsRepository.save(
                Parts.builder()
                        .category(PartsCategory.WHEEL)
                        .build()
        );

        LotteryApplierParts lotteryApplierParts1 = lotteryApplierPartsRepository.save(
                LotteryApplierParts.createApplierParts(true, lotteryApplier, parts1)
        );

        whenPartsEquippedStatusIsChanged(parts1.getId());

        Assertions.assertThat(lotteryApplierParts1.isEquipped()).isFalse();

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
