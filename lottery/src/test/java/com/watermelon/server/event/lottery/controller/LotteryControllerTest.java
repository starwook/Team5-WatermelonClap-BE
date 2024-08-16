package com.watermelon.server.event.lottery.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.watermelon.server.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static com.epages.restdocs.apispec.ResourceDocumentation.*;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(LotteryController.class)
@DisplayName("[단위] 추첨 컨트롤러")
class LotteryControllerTest extends ControllerTest {

    @Test
    @DisplayName("당첨자 명단 반환 - 성공")
    void testGetOrderEventResultSuccess() throws Exception {

        givenLotteryWinners();

        whenLotteryWinnersAreRetrieved();

        thenLotteryWinnersAreRetrieved();

        resultActions
                .andDo(document("event/lotteries",
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag(TAG_LOTTERY)
                                        .description("당첨자 명단 조회")
                                        .build()
                        )
                ));
    }

    @Test
    @DisplayName("당첨자 정보 반환 - 성공")
    void testGetOrderEventResultFailure() throws Exception {

        givenLotteryWinnerInfo();

        whenLotteryWinnerInfoIsRetrieved();

        thenLotteryWinnerInfoIsRetrieved();

        resultActions
                .andDo(document("event/lotteries/info",
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag(TAG_LOTTERY)
                                        .description("당첨자 정보 조회")
                                        .build()
                        )));
    }


    @Test
    @DisplayName("당첨자 정보 저장 - 성공")
    void testCreateLotteryWinnerInfoSuccess() throws Exception {

        whenLotteryWinnerInfoIsAdded();

        thenLotteryWinnerInfoIsAdded();

        resultActions
                .andDo(print())
                .andDo(document("event/lotteries/info/create",
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag(TAG_LOTTERY)
                                        .description("당첨자 정보 입력")
                                        .build()
                        )));
    }

    @Test
    @DisplayName("응모 정보 반환 - 응모 안했을 경우")
    void testGetLotteryRankNotAppliedCase() throws Exception {

        givenLotteryApplierNotExist();

        whenLotteryAppliersRankIsRetrieved();

        thenLotteryAppliersRankIsRetrievedWithNoInfo();

        resultActions
                .andDo(document("응모 안했을 경우",
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag(TAG_LOTTERY)
                                        .description("응모 정보 조회")
                                        .build()
                        )));
    }

    @Test
    @DisplayName("응모 정보 반환 - 추첨 이벤트만 당첨된 경우")
    void testGetLotteryRankWinnerCase() throws Exception {

        givenLotteryWinner();

        whenLotteryAppliersRankIsRetrieved();

        thenLotteryAppliersRankIsRetrievedForLotteryWinner();

        resultActions
                .andDo(document("추첨 이벤트만 당첨된 경우",
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag(TAG_LOTTERY)
                                        .description("응모 정보 조회")
                                        .build()
                        )));

    }


    @Test
    @DisplayName("응모 정보 반환 - 응모했지만 당첨되지 않은 경우")
    void testGetLotteryRankAppliedButNotWinnerCase() throws Exception {

        givenLotteryApplierApplied();

        whenLotteryAppliersRankIsRetrieved();

        thenLotteryAppliersRankIsRetrievedForApplier();

        resultActions
                .andDo(document("응모했지만 당첨되지 않은 경우",
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag(TAG_LOTTERY)
                                        .description("응모 정보 조회")
                                        .build()
                        )));

    }

    @Test
    @DisplayName("응모 정보 반환 - 파츠 이벤트만 당첨된 경우")
    void testGetLotteryRankPartsWinnerCase() throws Exception {

        givenOnlyPartsWinner();

        whenLotteryAppliersRankIsRetrieved();

        thenLotteryAppliersRankIsRetrievedForPartsWinner();

        resultActions
                .andDo(document("파츠 이벤트만 당첨된 경우",
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag(TAG_LOTTERY)
                                        .description("응모 정보 조회")
                                        .build()
                        )));

    }

    @Test
    @DisplayName("응모 정보 반환 - 추첨, 파츠 둘다 당첨된 경우")
    void testGetLotteryRankBothWinnerCase() throws Exception {

        givenLotteryAndPartsWinner();

        whenLotteryAppliersRankIsRetrieved();

        thenLotteryAppliersRankIsRetrievedForBothWinner();

        resultActions
                .andDo(document("추첨, 파츠 둘다 당첨된 경우",
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag(TAG_LOTTERY)
                                        .description("응모 정보 조회")
                                        .build()
                        )));

    }

    @Test
    @DisplayName("추첨이벤트 경품 정보 반환 - 성공")
    void getRewardInfo() throws Exception {

        givenLotteryRewardInfo();

        whenLotteryRewardInfoIsRetrieved();

        thenLotteryRewardInfoIsRetrieved();

        resultActions
                .andDo(document("event/lotteries/rank",
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag(TAG_LOTTERY)
                                        .description("추첨이벤트 경품 정보 조회")
                                        .build()
                        )));
    }

    @Test
    @DisplayName("추첨이벤트 경품 정보 반환 - 경품 정보 없을 시")
    void getRewardInfoNotExistCase() throws Exception {

        givenLotteryRewardInfoNotExists();

        whenLotteryRewardInfoIsRetrieved();

        thenClientError();

        resultActions
                .andDo(document("경품 정보 없는 경우",
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag(TAG_LOTTERY)
                                        .description("추첨이벤트 경품 정보 조회")
                                        .build()
                        )));

    }

}