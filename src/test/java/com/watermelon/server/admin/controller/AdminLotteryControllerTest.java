package com.watermelon.server.admin.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.watermelon.server.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.watermelon.server.Constants.*;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminLotteryController.class)
@DisplayName("[단위] 추첨 어드민 컨트롤러")
class AdminLotteryControllerTest extends ControllerTest {

    @Test
    @DisplayName("응모자 명단 반환 - 성공")
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
    @DisplayName("추첨 당첨자 명단 반환 - 성공")
    void getLotteryWinners() throws Exception {

        givenLotteryWinnersForAdmin();

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
    @DisplayName("파츠 당첨자 명단 반환 - 성공")
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
    @DisplayName("추첨 당첨자 확인처리 - 성공")
    void lotteryWinnerCheckDone() throws Exception {

        whenLotteryWinnerCheck();

        resultActions
                .andExpect(status().isOk())
                .andDo(document(DOCUMENT_NAME_ADMIN_LOTTERY_WINNER_CHECK,
                        resourceSnippetAuthed("추첨 당첨자 확인처리")));

    }

    @Test
    @DisplayName("파츠 추첨 당첨자 확인처리 - 성공")
    void partsWinnerCheckDone() throws Exception {

        whenPartsWinnerCheck();

        resultActions
                .andExpect(status().isOk())
                .andDo(document(DOCUMENT_NAME_ADMIN_PARTS_WINNER_CHECK,
                        resourceSnippetAuthed("파츠 추첨 당첨자 확인처리")));

    }

    @Test
    @DisplayName("추첨 이벤트 추첨 - 성공")
    void lottery() throws Exception {

        whenLottery();

        resultActions
                .andExpect(status().isOk())
                .andDo(document(DOCUMENT_NAME_LOTTERY,
                        resourceSnippetAuthed("추첨 이벤트 응모자에 대해 추첨")));

    }

    @Test
    @DisplayName("파츠 이벤트 추첨 - 성공")
    void partsLottery() throws Exception {

        whenPartsLottery();

        resultActions
                .andExpect(status().isOk())
                .andDo(document(DOCUMENT_NAME_PARTS_LOTTERY,
                        resourceSnippetAuthed("파츠 이벤트 응모자에 대해 추첨")));

    }

}