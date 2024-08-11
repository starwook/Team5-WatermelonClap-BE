package com.watermelon.server.lottery.controller;

import com.epages.restdocs.apispec.FieldDescriptors;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.watermelon.server.ControllerTest;
import com.watermelon.server.event.lottery.controller.LotteryController;
import com.watermelon.server.event.lottery.dto.request.RequestLotteryWinnerInfoDto;
import com.watermelon.server.event.lottery.dto.response.ResponseLotteryRankDto;
import com.watermelon.server.event.lottery.dto.response.ResponseLotteryWinnerDto;
import com.watermelon.server.event.lottery.dto.response.ResponseLotteryWinnerInfoDto;
import com.watermelon.server.event.lottery.dto.response.ResponseRewardInfoDto;
import com.watermelon.server.event.lottery.service.LotteryRewardService;
import com.watermelon.server.event.lottery.service.LotteryService;
import com.watermelon.server.event.lottery.service.LotteryWinnerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.NoSuchElementException;

import static com.epages.restdocs.apispec.ResourceDocumentation.*;
import static com.watermelon.server.Constants.*;

import static org.mockito.ArgumentMatchers.anyString;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LotteryController.class)
@DisplayName("[단위] 추첨 컨트롤러")
class LotteryControllerTest extends ControllerTest {

    @Test
    @DisplayName("당첨자 명단 반환 - 성공")
    void testGetOrderEventResultSuccess() throws Exception {

        givenLotteryWinners();

        whenLotteryWinnersAreRetrieved();

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].email").isString())
                .andExpect(jsonPath("[0].rank").isNumber())
                .andDo(document("event/lotteries", resource("당첨자 명단 조회")));

    }

    @Test
    @DisplayName("당첨자 정보 반환 - 성공")
    void testGetOrderEventResultFailure() throws Exception {

        givenLotteryWinnerInfo();

        whenLotteryWinnerInfoIsRetrieved();

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").isString())
                .andExpect(jsonPath("address").isString())
                .andExpect(jsonPath("phoneNumber").isString())
                .andDo(document("event/lotteries/info", resource("당첨자 정보 조회")));

    }


    @Test
    @DisplayName("당첨자 정보 저장 - 성공")
    void testCreateLotteryWinnerInfoSuccess() throws Exception {

        whenLotteryWinnerInfoIsAdded();

        resultActions
                .andExpect(status().isCreated())
                .andDo(document("event/lotteries/info/create", resource("당첨자 정보 입력")));

    }

    @Test
    @DisplayName("응모 정보 반환 - 정보 없음")
    void testGetLotteryRankNotAppliedCase() throws Exception {

        givenLotteryApplierNotExist();

        whenLotteryAppliersRankIsRetrieved();

        resultActions
                .andExpect(jsonPath("rank").value(-1))
                .andExpect(jsonPath("applied").value(false))
                .andDo(document("event/lotteries/rank/success",
                        resourceSnippetAuthed("응모 정보 조회"))
                );

    }

    @Test
    @DisplayName("응모 정보 반환 - 성공")
    void testGetLotteryRankAppliedCase() throws Exception {

        givenLotteryWinner();

        whenLotteryAppliersRankIsRetrieved();

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("rank").value(TEST_RANK))
                .andExpect(jsonPath("applied").value(true))
                .andDo(document("event/lotteries/rank/failure",
                        resourceSnippetAuthed("응모 정보 조회")));

    }

    @Test
    @DisplayName("추첨이벤트 경품 정보 반환 - 성공")
    void getRewardInfo() throws Exception {

        givenLotteryRewardInfo();

        whenLotteryRewardInfoIsRetrieved();

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("imgSrc").isString())
                .andExpect(jsonPath("name").isString())
                .andDo(document("event/lotteries/rank",
                        resourceSnippet("추첨이벤트 경품 정보 조회")));

    }

}