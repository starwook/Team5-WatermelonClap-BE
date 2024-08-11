package com.watermelon.server.admin.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.watermelon.server.ControllerTest;
import com.watermelon.server.admin.dto.response.ResponseAdminLotteryWinnerDto;
import com.watermelon.server.admin.dto.response.ResponseAdminPartsWinnerDto;
import com.watermelon.server.admin.dto.response.ResponseLotteryApplierDto;
import com.watermelon.server.event.lottery.parts.service.PartsService;
import com.watermelon.server.event.lottery.service.LotteryService;
import com.watermelon.server.event.lottery.service.LotteryWinnerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.watermelon.server.Constants.*;
import static org.mockito.Mockito.verify;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminLotteryController.class)
class AdminLotteryControllerTest extends ControllerTest {

    @MockBean
    private LotteryService lotteryService;

    @MockBean
    private LotteryWinnerService lotteryWinnerService;

    @MockBean
    private PartsService partsService;

    @Test
    @DisplayName("응모자 명단을 반환한다.")
    void getLotteryAppliers() throws Exception {

        givenLotteryApplierList();

        whenLotteryApplierListAreRetrievedForAdmin();

        resultActions
                .andExpect(jsonPath("content[0].uid").isString())
                .andExpect(jsonPath("content[0].link").isString())
                .andExpect(jsonPath("content[0].remainChance").isNumber())
                .andExpect(jsonPath("content[0].rank").isNumber())
                .andDo(print())
                .andDo(document(DOCUMENT_NAME_ADMIN_APPLIER,
                        resource(
                                ResourceSnippetParameters.builder()
                                        .description("응모자 명단 조회")
                                        .queryParameters(
                                                parameterWithName(PARAM_PAGE).description("페이지"),
                                                parameterWithName(PARAM_SIZE).description("페이지 크기")
                                        )
                                        .build()
                        )
                ));

    }

    @Test
    @DisplayName("추첨 당첨자 명단을 반환한다.")
    void getLotteryWinners() throws Exception {

        givenLotteryWinners();

        whenLotteryWinnerListAreRetrievedForAdmin();

        resultActions
                .andExpect(jsonPath("[0].uid").isString())
                .andExpect(jsonPath("[0].name").isString())
                .andExpect(jsonPath("[0].phoneNumber").isString())
                .andExpect(jsonPath("[0].address").isString())
                .andExpect(jsonPath("[0].rank").isNumber())
                .andExpect(jsonPath("[0].status").isString())
                .andDo(document(DOCUMENT_NAME_LOTTERY_WINNERS, resourceSnippetAuthed("추첨 당첨자 명단 조회")));

    }

    @Test
    @DisplayName("파츠 당첨자 명단을 반환한다.")
    void getPartsWinners() throws Exception {

        givenPartsWinnerList();

        whenPartsWinnerListAreRetrievedForAdmin();

        resultActions
                .andExpect(jsonPath("[0].uid").isString())
                .andExpect(jsonPath("[0].name").isString())
                .andExpect(jsonPath("[0].phoneNumber").isString())
                .andExpect(jsonPath("[0].address").isString())
                .andExpect(jsonPath("[0].rank").isNumber())
                .andExpect(jsonPath("[0].status").isString())
                .andDo(document(DOCUMENT_NAME_PARTS_WINNERS, resourceSnippetAuthed("파츠 당첨자 명단 조회")));

    }

    @Test
    @DisplayName("추첨 당첨자를 확인처리 한다.")
    void lotteryWinnerCheckDone() throws Exception {

        whenLotteryWinnerCheck();

        resultActions
                .andExpect(status().isOk())
                .andDo(document(DOCUMENT_NAME_ADMIN_LOTTERY_WINNER_CHECK,
                        resourceSnippetAuthed("추첨 당첨자 확인처리")));

    }

    @Test
    @DisplayName("파츠 추첨 당첨자를 확인처리 한다.")
    void partsWinnerCheckDone() throws Exception {

        whenPartsWinnerCheck();

        resultActions
                .andExpect(status().isOk())
                .andDo(document(DOCUMENT_NAME_ADMIN_PARTS_WINNER_CHECK,
                        resourceSnippetAuthed("파츠 추첨 당첨자 확인처리")));

    }

    @Test
    @DisplayName("추첨 이벤트 응모자에 대해 추첨을 진행한다.")
    void lottery() throws Exception {

        whenLottery();

        resultActions
                .andExpect(status().isOk())
                .andDo(document(DOCUMENT_NAME_LOTTERY,
                        resourceSnippetAuthed("추첨 이벤트 응모자에 대해 추첨")));

    }

    @Test
    @DisplayName("파츠 이벤트 응모자에 대해 추첨을 진행한다.")
    void partsLottery() throws Exception {

        whenPartsLottery();

        resultActions
                .andExpect(status().isOk())
                .andDo(document(DOCUMENT_NAME_PARTS_LOTTERY,
                        resourceSnippetAuthed("파츠 이벤트 응모자에 대해 추첨")));

    }

    private void givenLotteryApplierList() {
        Pageable pageable = PageRequest.of(TEST_PAGE_NUMBER, TEST_PAGE_SIZE);

        Mockito.when(lotteryService.getApplierInfoPage(pageable))
                .thenReturn(new PageImpl<>(
                        List.of(
                                ResponseLotteryApplierDto.createTestDto(),
                                ResponseLotteryApplierDto.createTestDto()
                        ), pageable, TEST_PAGE_SIZE));
    }

    private void givenLotteryWinners() {

        Mockito.when(lotteryWinnerService.getAdminLotteryWinners())
                .thenReturn(List.of(
                                ResponseAdminLotteryWinnerDto.createTestDto(),
                                ResponseAdminLotteryWinnerDto.createTestDto()
                        )
                );
    }

    private void givenPartsWinnerList(){
        Mockito.when(partsService.getAdminPartsWinners())
                .thenReturn(List.of(
                        ResponseAdminPartsWinnerDto.createTestDto(),
                        ResponseAdminPartsWinnerDto.createTestDto()
                ));
    }

    private void whenLotteryApplierListAreRetrievedForAdmin() throws Exception {
        resultActions = mockMvc.perform(get(PATH_ADMIN_APPLIER)
                .header(HEADER_NAME_AUTHORIZATION, HEADER_VALUE_BEARER + " " + TEST_TOKEN)
                .param(PARAM_PAGE, String.valueOf(TEST_PAGE_NUMBER))
                .param(PARAM_SIZE, String.valueOf(TEST_PAGE_SIZE))
        );
    }

    private void whenLotteryWinnerListAreRetrievedForAdmin() throws Exception {
        resultActions = mockMvc.perform(get(PATH_ADMIN_LOTTERY_WINNERS)
                .header(HEADER_NAME_AUTHORIZATION, HEADER_VALUE_BEARER + " " + TEST_TOKEN)
        );
    }

    private void whenPartsWinnerListAreRetrievedForAdmin() throws Exception {
        resultActions = mockMvc.perform(get(PATH_ADMIN_PARTS_WINNERS)
                .header(HEADER_NAME_AUTHORIZATION, HEADER_VALUE_BEARER + " " + TEST_TOKEN));
    }

    private void whenLotteryWinnerCheck() throws Exception {
        resultActions = mockMvc.perform(post(PATH_ADMIN_LOTTERY_WINNER_CHECK, TEST_UID)
                .header(HEADER_NAME_AUTHORIZATION, HEADER_VALUE_BEARER + " " + TEST_TOKEN)
        );
        verify(lotteryWinnerService).lotteryWinnerCheck(TEST_UID);
    }

    private void whenPartsWinnerCheck() throws Exception {
        resultActions = mockMvc.perform(post(PATH_ADMIN_PARTS_WINNER_CHECK, TEST_UID)
                .header(HEADER_NAME_AUTHORIZATION, HEADER_VALUE_BEARER + " " + TEST_TOKEN)
        );
        verify(partsService).partsWinnerCheck(TEST_UID);
    }

    private void whenLottery() throws Exception {
        resultActions = mockMvc.perform(post(PATH_LOTTERY)
                .header(HEADER_NAME_AUTHORIZATION, HEADER_VALUE_BEARER + " " + TEST_TOKEN));

        verify(lotteryService).lottery();
    }

    private void whenPartsLottery() throws Exception {
        resultActions = mockMvc.perform(post(PATH_PARTS_LOTTERY)
                .header(HEADER_NAME_AUTHORIZATION, HEADER_VALUE_BEARER + " " + TEST_TOKEN));

        verify(partsService).partsLottery();
    }

}