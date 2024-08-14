package com.watermelon.server.event.admin.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.watermelon.server.ControllerTest;
import com.watermelon.server.admin.controller.AdminLotteryController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.watermelon.server.constants.Constants.*;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(AdminLotteryController.class)
@DisplayName("[단위] 추첨 어드민 컨트롤러")
class AdminLotteryControllerTest extends ControllerTest {

    @Test
    @DisplayName("응모자 명단 반환 - 성공")
    void getLotteryAppliers() throws Exception {

        givenLotteryApplierList();

        whenLotteryApplierListAreRetrievedForAdmin();

        thenLotteryApplierListAreRetrievedForAdmin();

        resultActions
                .andDo(print())
                .andDo(document(DOCUMENT_NAME_ADMIN_APPLIER,
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag(TAG_LOTTERY)
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

        thenLotteryWinnerListAreRetrievedForAdmin();

        resultActions
                .andDo(document(DOCUMENT_NAME_LOTTERY_WINNERS,
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag(TAG_LOTTERY)
                                        .description("추첨 당첨자 명단 조회")
                                        .build()
                        )));

    }

    @Test
    @DisplayName("파츠 당첨자 명단 반환 - 성공")
    void getPartsWinners() throws Exception {

        givenPartsWinnerList();

        whenPartsWinnerListAreRetrievedForAdmin();

        thenPartsWinnerListAreRetrievedForAdmin();

        resultActions
                .andDo(document(DOCUMENT_NAME_PARTS_WINNERS,
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag(TAG_PARTS)
                                        .description("파츠 당첨자 명단 조회")
                                        .build()
                        )));

    }

    @Test
    @DisplayName("추첨 당첨자 확인처리 - 성공")
    void lotteryWinnerCheckDone() throws Exception {

        whenLotteryWinnerCheck();

        thenLotteryWinnerCheck();

        resultActions
                .andDo(document(DOCUMENT_NAME_ADMIN_LOTTERY_WINNER_CHECK,
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag(TAG_LOTTERY)
                                        .description("추첨 당첨자 확인처리")
                                        .build()
                        )));
    }

    @Test
    @DisplayName("파츠 추첨 당첨자 확인처리 - 성공")
    void partsWinnerCheckDone() throws Exception {

        whenPartsWinnerCheck();

        thenPartsWinnerCheck();

        resultActions
                .andDo(document(DOCUMENT_NAME_ADMIN_PARTS_WINNER_CHECK,
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag(TAG_PARTS)
                                        .description("파츠 추첨 당첨자 확인처리")
                                        .build()
                        )));
    }

    @Test
    @DisplayName("추첨 이벤트 추첨 - 성공")
    void lottery() throws Exception {

        whenLottery();

        thenLottery();

        resultActions
                .andDo(document(DOCUMENT_NAME_LOTTERY,
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag(TAG_LOTTERY)
                                        .description("추첨 이벤트 응모자에 대해 추첨")
                                        .build()
                        )));
    }

    @Test
    @DisplayName("파츠 이벤트 추첨 - 성공")
    void partsLottery() throws Exception {

        whenPartsLottery();

        thenPartsLottery();

        resultActions
                .andDo(document(DOCUMENT_NAME_PARTS_LOTTERY,
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag(TAG_PARTS)
                                        .description("파츠 이벤트 응모자에 대해 추첨")
                                        .build()
                        )));
    }

    @Test
    @DisplayName("추첨 이벤트 생성 - 성공")
    void createLotteryEvent() throws Exception {

        whenLotteryEventCreate();

        thenLotteryEventCreate();

        resultActions
                .andDo(print())
                .andDo(document("admin/event/lotteries/create",
                        resource(
                                ResourceSnippetParameters.builder()
                                        .requestFields(
                                        )
                                        .tag(TAG_LOTTERY)
                                        .description("추첨 이벤트 생성")
                                        .build()
                        )));

    }
}