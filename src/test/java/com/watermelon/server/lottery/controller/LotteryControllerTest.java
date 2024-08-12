package com.watermelon.server.lottery.controller;

import com.epages.restdocs.apispec.ResourceSnippet;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.watermelon.server.ControllerTest;
import com.watermelon.server.event.lottery.controller.LotteryController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static com.epages.restdocs.apispec.ResourceDocumentation.*;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;

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
                .andDo(document("event/lotteries/info/create",
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag(TAG_LOTTERY)
                                        .description("당첨자 정보 입력")
                                        .build()
                        )));
    }

    @Test
    @DisplayName("응모 정보 반환 - 정보 없음")
    void testGetLotteryRankNotAppliedCase() throws Exception {

        givenLotteryApplierNotExist();

        whenLotteryAppliersRankIsRetrieved();

        thenLotteryAppliersRankIsRetrievedWithNoInfo();

        resultActions
                .andDo(document("event/lotteries/rank/success",
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag(TAG_LOTTERY)
                                        .description("응모 정보 조회")
                                        .build()
                        )));
    }

    @Test
    @DisplayName("응모 정보 반환 - 성공")
    void testGetLotteryRankAppliedCase() throws Exception {

        givenLotteryWinner();

        whenLotteryAppliersRankIsRetrieved();

        thenLotteryAppliersRankIsRetrievedForWinner();

        resultActions
                .andDo(document("event/lotteries/rank/failure",
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

}