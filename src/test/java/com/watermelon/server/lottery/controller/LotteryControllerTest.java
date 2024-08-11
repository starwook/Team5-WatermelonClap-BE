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

    @MockBean
    private LotteryService lotteryService;

    @MockBean
    private LotteryWinnerService lotteryWinnerService;

    @MockBean
    private LotteryRewardService lotteryRewardService;

    @Test
    @DisplayName("당첨자 명단을 반환한다.")
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
    @DisplayName("당첨자 정보를 반환한다.")
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
    @DisplayName("당첨자 정보가 성공적으로 저장되면 201 Status 로 응답한다.")
    void testCreateLotteryWinnerInfoSuccess() throws Exception {

        whenLotteryWinnerInfoIsAdded();

        resultActions
                .andExpect(status().isCreated())
                .andDo(document("event/lotteries/info/create", resource("당첨자 정보 입력")));

    }

    @Test
    @DisplayName("응모 정보가 없으면 rank : -1, applied : false 로 응답한다.")
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
    @DisplayName("응모 정보가 있으면 해당 유저의 rank, applied : true 로 응답한다.")
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
    @DisplayName("추첨이벤트 경품 정보를 반환한다.")
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

    private void givenLotteryRewardInfo(){
        Mockito.when(lotteryRewardService.getRewardInfo(TEST_RANK)).thenReturn(
                new ResponseRewardInfoDto(TEST_IMGSRC, TEST_NAME)
        );
    }

    private void givenLotteryWinners() {
        Mockito.when(lotteryWinnerService.getLotteryWinners())
                .thenReturn(List.of(
                        ResponseLotteryWinnerDto.from("email2@email.com", 1)
                ));
    }


    private void givenLotteryWinnerInfo() {
        Mockito.when(lotteryWinnerService.getLotteryWinnerInfo(TEST_UID))
                .thenReturn(ResponseLotteryWinnerInfoDto.builder()
                        .name(TEST_NAME)
                        .address(TEST_ADDRESS)
                        .phoneNumber(TEST_PHONE_NUMBER)
                        .build());
    }

    private void givenLotteryApplierNotExist() {
        Mockito.doThrow(new NoSuchElementException()).when(lotteryService).getLotteryRank(anyString());
    }

    private void givenLotteryWinner(){
        Mockito.when(lotteryService.getLotteryRank(TEST_UID)).thenReturn(
                ResponseLotteryRankDto.createAppliedTest()
        );
    }

    private void whenLotteryAppliersRankIsRetrieved() throws Exception {
        resultActions = this.mockMvc.perform(get("/event/lotteries/rank")
                .header(HEADER_NAME_AUTHORIZATION, HEADER_VALUE_BEARER + HEADER_VALUE_SPACE + TEST_TOKEN));

    }

    private void whenLotteryRewardInfoIsRetrieved() throws Exception {
        resultActions = this.mockMvc.perform(get("/event/lotteries/reward/{rank}", TEST_RANK));
    }

    private void whenLotteryWinnersAreRetrieved() throws Exception {
        resultActions = mockMvc.perform(get("/event/lotteries"));
    }

    private void whenLotteryWinnerInfoIsRetrieved() throws Exception {
        resultActions = this.mockMvc.perform(get("/event/lotteries/info")
                .header(HEADER_NAME_AUTHORIZATION, HEADER_VALUE_BEARER + HEADER_VALUE_SPACE + TEST_TOKEN));

    }

    private void whenLotteryWinnerInfoIsAdded() throws Exception {
        RequestLotteryWinnerInfoDto requestLotteryWinnerInfoDto = RequestLotteryWinnerInfoDto.builder()
                .address(TEST_ADDRESS)
                .name(TEST_NAME)
                .phoneNumber(TEST_PHONE_NUMBER)
                .build();

        String requestJson = objectMapper.writeValueAsString(requestLotteryWinnerInfoDto);

        //when
        resultActions = this.mockMvc.perform(post("/event/lotteries/info")
                .header(HEADER_NAME_AUTHORIZATION, HEADER_VALUE_BEARER + HEADER_VALUE_SPACE + TEST_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson));
    }


}