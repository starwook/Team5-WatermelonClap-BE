package com.watermelon.server;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.watermelon.server.event.lottery.dto.request.RequestLotteryWinnerInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.watermelon.server.Constants.*;
import static com.watermelon.server.Constants.TEST_TOKEN;
import static com.watermelon.server.common.constants.PathConstants.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;

/**
 * API 기반으로 한 테스트에서 사용되는 메소드들을 공통화한 부모 클래스
 */
@AutoConfigureMockMvc
public abstract class APITest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    protected ResultActions resultActions;

    protected abstract void givenLotteryRewardInfo();
    protected abstract void givenLotteryWinners();
    protected abstract void givenLotteryWinnersForAdmin();
    protected abstract void givenLotteryWinnerInfo();
    protected abstract void givenLotteryApplierNotExist();
    protected abstract void givenLotteryWinner();

    protected abstract void givenPartsListForUri();
    protected abstract void givenLotteryApplierWhoHasNoRemainChance();
    protected abstract void givenLotteryApplierWhoDrawsPartsFirst();
    protected abstract void givenLotteryApplierWhoHasRemainChance();
    protected abstract void givenMyPartsList();

    protected abstract void givenLink();
    protected abstract void givenOriginUri();

    protected void whenLotteryAppliersRankIsRetrieved() throws Exception {
        resultActions = this.mockMvc.perform(get("/event/lotteries/rank")
                .header(HEADER_NAME_AUTHORIZATION, HEADER_VALUE_BEARER + HEADER_VALUE_SPACE + TEST_TOKEN));

    }

    protected void whenLotteryRewardInfoIsRetrieved() throws Exception {
        resultActions = this.mockMvc.perform(get("/event/lotteries/reward/{rank}", TEST_RANK));
    }

    protected void whenLotteryWinnersAreRetrieved() throws Exception {
        resultActions = mockMvc.perform(get("/event/lotteries"));
    }

    protected void whenLotteryWinnerInfoIsRetrieved() throws Exception {
        resultActions = this.mockMvc.perform(get("/event/lotteries/info")
                .header(HEADER_NAME_AUTHORIZATION, HEADER_VALUE_BEARER + HEADER_VALUE_SPACE + TEST_TOKEN));

    }

    protected void whenLotteryWinnerInfoIsAdded() throws Exception {
        RequestLotteryWinnerInfoDto requestLotteryWinnerInfoDto = RequestLotteryWinnerInfoDto.builder()
                .address(TEST_ADDRESS)
                .name(TEST_NAME)
                .phoneNumber(TEST_PHONE_NUMBER)
                .build();

        String requestJson = objectMapper.writeValueAsString(requestLotteryWinnerInfoDto);

        resultActions = this.mockMvc.perform(post("/event/lotteries/info")
                .header(HEADER_NAME_AUTHORIZATION, HEADER_VALUE_BEARER + HEADER_VALUE_SPACE + TEST_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson));
    }

    protected void whenLotteryApplierListAreRetrievedForAdmin() throws Exception {
        resultActions = mockMvc.perform(get(PATH_ADMIN_APPLIER)
                .header(HEADER_NAME_AUTHORIZATION, HEADER_VALUE_BEARER + " " + TEST_TOKEN)
                .param(PARAM_PAGE, String.valueOf(TEST_PAGE_NUMBER))
                .param(PARAM_SIZE, String.valueOf(TEST_PAGE_SIZE))
        );
    }

    protected void whenLotteryWinnerListAreRetrievedForAdmin() throws Exception {
        resultActions = mockMvc.perform(get(PATH_ADMIN_LOTTERY_WINNERS)
                .header(HEADER_NAME_AUTHORIZATION, HEADER_VALUE_BEARER + " " + TEST_TOKEN)
        );
    }

    protected void whenPartsWinnerListAreRetrievedForAdmin() throws Exception {
        resultActions = mockMvc.perform(get(PATH_ADMIN_PARTS_WINNERS)
                .header(HEADER_NAME_AUTHORIZATION, HEADER_VALUE_BEARER + " " + TEST_TOKEN));
    }

    protected void whenLotteryWinnerCheck() throws Exception {
        resultActions = mockMvc.perform(post(PATH_ADMIN_LOTTERY_WINNER_CHECK, TEST_UID)
                .header(HEADER_NAME_AUTHORIZATION, HEADER_VALUE_BEARER + " " + TEST_TOKEN)
        );
    }

    protected void whenPartsWinnerCheck() throws Exception {
        resultActions = mockMvc.perform(post(PATH_ADMIN_PARTS_WINNER_CHECK, TEST_UID)
                .header(HEADER_NAME_AUTHORIZATION, HEADER_VALUE_BEARER + " " + TEST_TOKEN)
        );
    }

    protected void whenLottery() throws Exception {
        resultActions = mockMvc.perform(post(PATH_LOTTERY)
                .header(HEADER_NAME_AUTHORIZATION, HEADER_VALUE_BEARER + " " + TEST_TOKEN));
    }

    protected void whenPartsLottery() throws Exception {
        resultActions = mockMvc.perform(post(PATH_PARTS_LOTTERY)
                .header(HEADER_NAME_AUTHORIZATION, HEADER_VALUE_BEARER + " " + TEST_TOKEN));
    }


    protected void whenDrawParts() throws Exception {
        resultActions = mockMvc.perform(post("/event/parts")
                .header(HEADER_NAME_AUTHORIZATION, HEADER_VALUE_BEARER + HEADER_VALUE_SPACE + TEST_TOKEN));
    }

    protected void whenPartsEquippedStatusIsChanged() throws Exception {
        resultActions = mockMvc.perform(patch("/event/parts/{parts_id}", TEST_PARTS_ID)
                .header(HEADER_NAME_AUTHORIZATION, HEADER_VALUE_BEARER + HEADER_VALUE_SPACE + TEST_TOKEN));

    }

    protected void whenMyRemainChanceIsRetrieved() throws Exception {
        resultActions = mockMvc.perform(get("/event/parts/remain")
                .header(HEADER_NAME_AUTHORIZATION, HEADER_VALUE_BEARER + HEADER_VALUE_SPACE + TEST_TOKEN));

    }

    protected void whenMyPartsListAreRetrieved() throws Exception {
        resultActions = mockMvc.perform(get("/event/parts")
                .header(HEADER_NAME_AUTHORIZATION, HEADER_VALUE_BEARER + HEADER_VALUE_SPACE + TEST_TOKEN));
    }

    protected void whenPartsListAreRetrievedWithUri() throws Exception {
        resultActions = mockMvc.perform(get(PARTS_LINK_LIST, TEST_URI)
                .header(HEADER_NAME_AUTHORIZATION, HEADER_VALUE_BEARER + HEADER_VALUE_SPACE + TEST_TOKEN));
    }

    protected void whenLinkIsRetrieved() throws Exception {
        resultActions = mockMvc.perform(get(MY_LINK)
                .header(HEADER_NAME_AUTHORIZATION, HEADER_VALUE_BEARER + HEADER_VALUE_SPACE + TEST_TOKEN));
    }

    protected void whenRedirect() throws Exception {
        resultActions = mockMvc.perform(get(SHORTED_LINK, TEST_SHORTED_URI));
    }


}
