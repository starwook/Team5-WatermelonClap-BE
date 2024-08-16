package com.watermelon.server;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.watermelon.server.event.lottery.dto.request.RequestLotteryEventDto;
import com.watermelon.server.event.lottery.dto.request.RequestLotteryWinnerInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.nio.charset.StandardCharsets;

import static com.watermelon.server.constants.Constants.*;
import static com.watermelon.server.common.constants.PathConstants.*;
import static com.watermelon.server.auth.service.TestTokenVerifier.TEST_UID;
import static com.watermelon.server.auth.service.TestTokenVerifier.TEST_VALID_TOKEN;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * API 기반으로 한 테스트에서 사용되는 메소드들을 공통화한 부모 클래스
 */
@AutoConfigureMockMvc
public abstract class APITest {

    @Autowired
    protected MockMvc mvc;

    @Autowired
    protected ObjectMapper objectMapper;

    protected ResultActions resultActions;

    protected abstract void givenLotteryRewardInfo();
    protected abstract void givenLotteryWinners();
    protected abstract void givenLotteryWinnersForAdmin();
    protected abstract void givenLotteryWinnerInfo();
    protected abstract void givenLotteryApplierNotExist();
    protected abstract void givenLotteryApplierApplied();
    protected abstract void givenLotteryWinner();

    protected abstract void givenPartsListForUri();
    protected abstract void givenPartsNotEquipped();
    protected abstract void givenLotteryApplierWhoHasNoRemainChance();
    protected abstract void givenLotteryApplierWhoDrawsPartsFirst();
    protected abstract void givenLotteryApplierWhoHasRemainChance();
    protected abstract void givenMyPartsList();

    protected abstract void givenLink();
    protected abstract void givenOriginUri();

    private MockHttpServletRequestBuilder authedRequest(MockHttpServletRequestBuilder requestBuilder) {
        return requestBuilder
                .header(HEADER_NAME_AUTHORIZATION, HEADER_VALUE_BEARER + HEADER_VALUE_SPACE + TEST_VALID_TOKEN);
    }

    protected void whenLotteryAppliersRankIsRetrieved() throws Exception {
        resultActions = mvc.perform(authedRequest(get("/event/lotteries/rank")));
    }

    protected void thenLotteryAppliersRankIsRetrievedForLotteryWinner() throws Exception {
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("rank").value(TEST_RANK))
                .andExpect(jsonPath("applied").value(true))
                .andExpect(jsonPath("miniature").value(false));
    }

    protected void thenLotteryAppliersRankIsRetrievedForApplier() throws Exception {
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("rank").value(-1))
                .andExpect(jsonPath("applied").value(true))
                .andExpect(jsonPath("miniature").value(false));
    }

    protected void thenLotteryAppliersRankIsRetrievedWithNoInfo() throws Exception {
        resultActions
                .andExpect(jsonPath("rank").value(-1))
                .andExpect(jsonPath("applied").value(false))
                .andExpect(jsonPath("miniature").value(false));
    }

    protected void thenLotteryAppliersRankIsRetrievedForPartsWinner() throws Exception {
        resultActions
                .andExpect(jsonPath("rank").value(-1))
                .andExpect(jsonPath("applied").value(true))
                .andExpect(jsonPath("miniature").value(true));
    }

    protected void thenLotteryAppliersRankIsRetrievedForBothWinner() throws Exception {
        resultActions
                .andExpect(jsonPath("rank").value(1))
                .andExpect(jsonPath("applied").value(true))
                .andExpect(jsonPath("miniature").value(true));
    }

    protected void whenLotteryRewardInfoIsRetrieved() throws Exception {
        resultActions = this.mvc.perform(get("/event/lotteries/reward/{rank}", TEST_RANK));
    }

    protected void thenLotteryRewardInfoIsRetrieved() throws Exception {
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("imgSrc").isString())
                .andExpect(jsonPath("name").isString());
    }

    protected void whenLotteryWinnersAreRetrieved() throws Exception {
        resultActions = mvc.perform(get("/event/lotteries"));
    }

    protected void thenLotteryWinnersAreRetrieved() throws Exception {
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].email").isString())
                .andExpect(jsonPath("[0].rank").isNumber());
    }

    protected void whenLotteryWinnerInfoIsRetrieved() throws Exception {
        resultActions = mvc.perform(authedRequest(get("/event/lotteries/info")));
    }

    protected void thenLotteryWinnerInfoIsRetrieved() throws Exception {
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").isString())
                .andExpect(jsonPath("address").isString())
                .andExpect(jsonPath("phoneNumber").isString());
    }

    protected void whenLotteryWinnerInfoIsAdded() throws Exception {
        RequestLotteryWinnerInfoDto requestLotteryWinnerInfoDto = RequestLotteryWinnerInfoDto.builder()
                .address(TEST_ADDRESS)
                .name(TEST_NAME)
                .phoneNumber(TEST_PHONE_NUMBER)
                .build();

        String requestJson = objectMapper.writeValueAsString(requestLotteryWinnerInfoDto);

        resultActions = this.mvc.perform(
                authedRequest(post("/event/lotteries/info"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson));
    }

    protected void thenLotteryWinnerInfoIsAdded() throws Exception {
        resultActions
                .andExpect(status().isCreated());
    }

    protected void whenLotteryApplierListAreRetrievedForAdmin() throws Exception {
        resultActions = mvc.perform(
                authedRequest(get(PATH_ADMIN_APPLIER))
                .param(PARAM_PAGE, String.valueOf(TEST_PAGE_NUMBER))
                .param(PARAM_SIZE, String.valueOf(TEST_PAGE_SIZE))
        );
    }

    protected void thenLotteryApplierListAreRetrievedForAdmin() throws Exception {
        resultActions
                .andExpect(jsonPath("content[0].uid").isString())
                .andExpect(jsonPath("content[0].link").isString())
                .andExpect(jsonPath("content[0].remainChance").isNumber())
                .andExpect(jsonPath("content[0].rank").isNumber());
    }

    protected void whenLotteryWinnerListAreRetrievedForAdmin() throws Exception {
        resultActions = mvc.perform(authedRequest(get(PATH_ADMIN_LOTTERY_WINNERS)));
    }

    protected void thenLotteryWinnerListAreRetrievedForAdmin() throws Exception {
        resultActions
                .andExpect(jsonPath("[0].uid").isString())
                .andExpect(jsonPath("[0].name").isString())
                .andExpect(jsonPath("[0].phoneNumber").isString())
                .andExpect(jsonPath("[0].address").isString())
                .andExpect(jsonPath("[0].rank").isNumber())
                .andExpect(jsonPath("[0].status").isString());
    }

    protected void whenPartsWinnerListAreRetrievedForAdmin() throws Exception {
        resultActions = mvc.perform(authedRequest(get(PATH_ADMIN_PARTS_WINNERS)));
    }

    protected void thenPartsWinnerListAreRetrievedForAdmin() throws Exception {
        resultActions
                .andExpect(jsonPath("[0].uid").isString())
                .andExpect(jsonPath("[0].name").isString())
                .andExpect(jsonPath("[0].phoneNumber").isString())
                .andExpect(jsonPath("[0].address").isString())
                .andExpect(jsonPath("[0].rank").isNumber())
                .andExpect(jsonPath("[0].status").isString());
    }

    protected void whenLotteryWinnerCheck() throws Exception {
        resultActions = mvc.perform(authedRequest(post(PATH_ADMIN_LOTTERY_WINNER_CHECK, TEST_UID)));
    }

    protected void thenLotteryWinnerCheck() throws Exception {
        resultActions
                .andExpect(status().isOk());
    }

    protected void whenPartsWinnerCheck() throws Exception {
        resultActions = mvc.perform(authedRequest(post(PATH_ADMIN_PARTS_WINNER_CHECK, TEST_UID)));
    }

    protected void thenPartsWinnerCheck() throws Exception {
        resultActions
                .andExpect(status().isOk());
    }

    protected void whenLottery() throws Exception {
        resultActions = mvc.perform(authedRequest(post(PATH_LOTTERY)));
    }

    protected void thenLottery() throws Exception {
        resultActions
                .andExpect(status().isOk());
    }

    protected void whenPartsLottery() throws Exception {
        resultActions = mvc.perform(authedRequest(post(PATH_PARTS_LOTTERY)));
    }

    protected void thenPartsLottery() throws Exception {
        resultActions
                .andExpect(status().isOk());
    }

    protected void whenDrawParts() throws Exception {
        resultActions = mvc.perform(authedRequest(post("/event/parts")));
    }

    protected void thenDrawParts() throws Exception {
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("category").isString())
                .andExpect(jsonPath("partsId").isNumber())
                .andExpect(jsonPath("name").isString())
                .andExpect(jsonPath("description").isString())
                .andExpect(jsonPath("imgSrc").isString())
                .andExpect(jsonPath("thumbnailImgSrc").isString())
                .andExpect(jsonPath("equipped").isBoolean());
    }

    protected void thenDrawPartsHasNotRemainChance() throws Exception {
        resultActions
                .andExpect(status().isTooManyRequests());
    }

    protected void whenPartsEquippedStatusIsChanged(Long partsId) throws Exception {
        resultActions = mvc.perform(authedRequest(patch("/event/parts/{parts_id}", partsId)));

    }

    protected void thenPartsEquippedStatusIsChanged() throws Exception {
        resultActions.andExpect(status().isOk());
    }

    protected void whenMyRemainChanceIsRetrieved() throws Exception {
        resultActions = mvc.perform(authedRequest(get("/event/parts/remain")));

    }

    protected void thenMyRemainChanceIsRetrieved() throws Exception {
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("remainChance").isNumber());
    }

    protected void whenMyPartsListAreRetrieved() throws Exception {
        resultActions = mvc.perform(authedRequest(get("/event/parts")));
    }

    protected void thenMyPartsListAreRetrieved() throws Exception {
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].category").isString())
                .andExpect(jsonPath("[0].parts[0].category").isString())
                .andExpect(jsonPath("[0].parts[0].partsId").isNumber())
                .andExpect(jsonPath("[0].parts[0].name").isString())
                .andExpect(jsonPath("[0].parts[0].description").isString())
                .andExpect(jsonPath("[0].parts[0].imgSrc").isString())
                .andExpect(jsonPath("[0].parts[0].thumbnailImgSrc").isString())
                .andExpect(jsonPath("[0].parts[0].equipped").isBoolean());
    }

    protected void whenPartsListAreRetrievedWithUri(String uri) throws Exception {
        resultActions = mvc.perform(authedRequest(get(PARTS_LINK_LIST, uri)));
    }

    protected void thenPartsListAreRetrievedWithUri() throws Exception {
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].category").isString())
                .andExpect(jsonPath("[0].parts[0].category").isString())
                .andExpect(jsonPath("[0].parts[0].partsId").isNumber())
                .andExpect(jsonPath("[0].parts[0].name").isString())
                .andExpect(jsonPath("[0].parts[0].description").isString())
                .andExpect(jsonPath("[0].parts[0].imgSrc").isString())
                .andExpect(jsonPath("[0].parts[0].thumbnailImgSrc").isString())
                .andExpect(jsonPath("[0].parts[0].equipped").isBoolean());
    }

    protected void whenLinkIsRetrieved() throws Exception {
        resultActions = mvc.perform(authedRequest(get(MY_LINK)));
    }

    protected void thenLinkIsRetrieved() throws Exception {
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("link").isString());
    }

    protected void whenRedirect() throws Exception {
        resultActions = mvc.perform(get(SHORTED_LINK, TEST_SHORTED_URI));
    }

    protected void thenRedirect() throws Exception {
        resultActions
                .andExpect(status().isFound())
                .andExpect(header().string(HEADER_NAME_LOCATION, TEST_REDIRECTION_URL));
    }

    protected void whenLotteryEventCreate() throws Exception {

        resultActions = mvc.perform(authedRequest(multipart("/admin/event/lotteries/create")
                .part(new MockPart("event", "event", objectMapper.writeValueAsString(RequestLotteryEventDto.createTest()).getBytes(StandardCharsets.UTF_8), MediaType.APPLICATION_JSON))
                .file(new MockMultipartFile("files", "image1.jpg", MediaType.IMAGE_JPEG_VALUE, "image1 content".getBytes()))
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept(MediaType.APPLICATION_JSON)));

    }

    protected void thenLotteryEventCreate() throws Exception {
        resultActions
                .andExpect(status().isOk());
    }

    protected void thenClientError() throws Exception {
        resultActions
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("reason").isString());
    }

    protected void thenServerError() throws Exception {
        resultActions
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("reason").isString());
    }

}
