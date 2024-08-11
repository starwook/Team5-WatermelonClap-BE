package com.watermelon.server.lottery.controller;

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
                .andDo(document("event/lotteries", resource("당첨자 명단 조회")));

    }

    @Test
    @DisplayName("당첨자 정보 반환 - 성공")
    void testGetOrderEventResultFailure() throws Exception {

        givenLotteryWinnerInfo();

        whenLotteryWinnerInfoIsRetrieved();

        thenLotteryWinnerInfoIsRetrieved();

        resultActions
                .andDo(document("event/lotteries/info", resource("당첨자 정보 조회")));

    }


    @Test
    @DisplayName("당첨자 정보 저장 - 성공")
    void testCreateLotteryWinnerInfoSuccess() throws Exception {

        whenLotteryWinnerInfoIsAdded();

        thenLotteryWinnerInfoIsAdded();

        resultActions
                .andDo(document("event/lotteries/info/create", resource("당첨자 정보 입력")));

    }

    @Test
    @DisplayName("응모 정보 반환 - 정보 없음")
    void testGetLotteryRankNotAppliedCase() throws Exception {

        givenLotteryApplierNotExist();

        whenLotteryAppliersRankIsRetrieved();

        thenLotteryAppliersRankIsRetrievedWithNoInfo();

        resultActions
                .andDo(document("event/lotteries/rank/success",
                        resourceSnippetAuthed("응모 정보 조회"))
                );

    }

    @Test
    @DisplayName("응모 정보 반환 - 성공")
    void testGetLotteryRankAppliedCase() throws Exception {

        givenLotteryWinner();

        whenLotteryAppliersRankIsRetrieved();

        thenLotteryAppliersRankIsRetrieved();

        resultActions
                .andDo(document("event/lotteries/rank/failure",
                        resourceSnippetAuthed("응모 정보 조회")));

    }

    @Test
    @DisplayName("추첨이벤트 경품 정보 반환 - 성공")
    void getRewardInfo() throws Exception {

        givenLotteryRewardInfo();

        whenLotteryRewardInfoIsRetrieved();

        thenLotteryRewardInfoIsRetrieved();

        resultActions
                .andDo(document("event/lotteries/rank",
                        resourceSnippet("추첨이벤트 경품 정보 조회")));

    }

}