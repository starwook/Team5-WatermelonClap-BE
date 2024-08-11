package com.watermelon.server.lottery.parts.controller;

import com.watermelon.server.ControllerTest;
import com.watermelon.server.DocumentConstants;
import com.watermelon.server.event.lottery.parts.controller.PartsController;
import com.watermelon.server.event.lottery.parts.dto.response.ResponseMyPartsListDto;
import com.watermelon.server.event.lottery.parts.dto.response.ResponsePartsDrawDto;
import com.watermelon.server.event.lottery.parts.dto.response.ResponseRemainChanceDto;
import com.watermelon.server.event.lottery.parts.exception.PartsDrawLimitExceededException;
import com.watermelon.server.event.lottery.parts.service.PartsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static com.watermelon.server.Constants.*;
import static com.watermelon.server.Constants.TEST_TOKEN;
import static com.watermelon.server.common.constants.PathConstants.PARTS_LINK_LIST;
import static org.mockito.Mockito.verify;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PartsController.class)
@DisplayName("[단위] 파츠 컨트롤러")
class PartsControllerTest extends ControllerTest {

    @MockBean
    private PartsService partsService;

    @Test
    @DisplayName("파츠 뽑기 결과를 반환한다.")
    void drawParts() throws Exception {

        givenLotteryApplierWhoDrawsPartsFirst();

        whenDrawParts();

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("category").isString())
                .andExpect(jsonPath("partsId").isNumber())
                .andExpect(jsonPath("name").isString())
                .andExpect(jsonPath("description").isString())
                .andExpect(jsonPath("imgSrc").isString())
                .andExpect(jsonPath("equipped").isBoolean())
                .andDo(document("event/parts/success",
                        resourceSnippetAuthed("파츠 뽑기")));
    }

    @Test
    @DisplayName("파츠 뽑기 횟수가 소진되었을 경우 429 에러를 반환한다.")
    void drawPartsException() throws Exception {

        givenLotteryApplierWhoHasNoRemainChance();

        whenDrawParts();

        resultActions
                .andExpect(status().isTooManyRequests())
                .andDo(document("event/parts/too-many-request",
                        resourceSnippetAuthed("파츠 뽑기")));

    }

    @Test
    @DisplayName("파츠 상태 변경에 성공")
    void toggleParts() throws Exception {

        whenPartsEquippedStatusIsChanged();

        resultActions
                .andExpect(status().isOk())
                .andDo(document("event/parts/equip",
                        resourceSnippetAuthed("자신의 파츠 상태 변경")));


    }

    @Test
    @DisplayName("남은 파츠 뽑기 횟수를 반환한다.")
    void getRemainChance() throws Exception {

        givenLotteryApplierWhoHasRemainChance();

        whenMyRemainChanceIsRetrieved();

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("remainChance").isNumber())
                .andDo(document("event/parts/remain",
                        resourceSnippetAuthed("자신의 남은 파츠 뽑기 횟수 조회")));

    }

    @Test
    @DisplayName("자신의 파츠 목록을 반환한다.")
    void getMyPartsList() throws Exception {

        givenMyPartsList();

        whenMyPartsListAreRetrieved();

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].category").isString())
                .andExpect(jsonPath("[0].parts[0].category").isString())
                .andExpect(jsonPath("[0].parts[0].partsId").isNumber())
                .andExpect(jsonPath("[0].parts[0].name").isString())
                .andExpect(jsonPath("[0].parts[0].description").isString())
                .andExpect(jsonPath("[0].parts[0].imgSrc").isString())
                .andExpect(jsonPath("[0].parts[0].equipped").isBoolean())
                .andDo(document("event/parts/get",
                        resourceSnippetAuthed("자신의 파츠 목록 조회")))
                .andDo(print());

    }

    @Test
    @DisplayName("링크 키의 주인에 대한 파츠 목록을 반환한다.")
    void getLinkPartsList() throws Exception {

        givenPartsListForUri();

        whenPartsListAreRetrievedWithUri();

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].category").isString())
                .andExpect(jsonPath("[0].parts[0].category").isString())
                .andExpect(jsonPath("[0].parts[0].partsId").isNumber())
                .andExpect(jsonPath("[0].parts[0].name").isString())
                .andExpect(jsonPath("[0].parts[0].description").isString())
                .andExpect(jsonPath("[0].parts[0].imgSrc").isString())
                .andExpect(jsonPath("[0].parts[0].equipped").isBoolean())
                .andDo(document(DocumentConstants.PARTS_LINK_LIST,
                        resourceSnippetAuthed("링크 키의 주인에 대한 파츠 목록 조회")));

    }

    private void givenPartsListForUri(){
        Mockito.when(partsService.getPartsList(TEST_URI)).thenReturn(
                ResponseMyPartsListDto.createTestDtoList()
        );
    }

    private void givenLotteryApplierWhoHasNoRemainChance(){
        Mockito.when(partsService.drawParts(TEST_UID)).thenThrow(new PartsDrawLimitExceededException());
    }

    private void givenLotteryApplierWhoDrawsPartsFirst(){
        Mockito.when(partsService.drawParts(TEST_UID)).thenReturn(
                ResponsePartsDrawDto.createResponsePartsDrawDtoTest()
        );
    }

    private void givenLotteryApplierWhoHasRemainChance() {
        Mockito.when(partsService.getRemainChance(TEST_UID)).thenReturn(
                ResponseRemainChanceDto.createTestDto()
        );
    }

    private void givenMyPartsList(){
        Mockito.when(partsService.getMyParts(TEST_UID)).thenReturn(
                ResponseMyPartsListDto.createTestDtoList()
        );
    }

    private void whenDrawParts() throws Exception {
        resultActions = mockMvc.perform(post("/event/parts")
                .header(HEADER_NAME_AUTHORIZATION, HEADER_VALUE_BEARER + HEADER_VALUE_SPACE + TEST_TOKEN));
    }

    private void whenPartsEquippedStatusIsChanged() throws Exception {
        resultActions = mockMvc.perform(patch("/event/parts/{parts_id}", TEST_PARTS_ID)
                .header(HEADER_NAME_AUTHORIZATION, HEADER_VALUE_BEARER + HEADER_VALUE_SPACE + TEST_TOKEN));

        verify(partsService).toggleParts(TEST_UID, TEST_PARTS_ID);

    }

    private void whenMyRemainChanceIsRetrieved() throws Exception {
        resultActions = mockMvc.perform(get("/event/parts/remain")
                .header(HEADER_NAME_AUTHORIZATION, HEADER_VALUE_BEARER + HEADER_VALUE_SPACE + TEST_TOKEN));

    }

    private void whenMyPartsListAreRetrieved() throws Exception {
        resultActions = mockMvc.perform(get("/event/parts")
                .header(HEADER_NAME_AUTHORIZATION, HEADER_VALUE_BEARER + HEADER_VALUE_SPACE + TEST_TOKEN));
    }

    private void whenPartsListAreRetrievedWithUri() throws Exception {
        resultActions = mockMvc.perform(get(PARTS_LINK_LIST, TEST_URI)
                .header(HEADER_NAME_AUTHORIZATION, HEADER_VALUE_BEARER + HEADER_VALUE_SPACE + TEST_TOKEN));
    }

}