package com.watermelon.server.event.parts.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.watermelon.server.ControllerTest;
import com.watermelon.server.constants.DocumentConstants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.watermelon.server.constants.Constants.TEST_PARTS_ID;
import static com.watermelon.server.constants.Constants.TEST_URI;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;

@WebMvcTest(PartsController.class)
@DisplayName("[단위] 파츠 컨트롤러")
class PartsControllerTest extends ControllerTest {

    @Test
    @DisplayName("파츠 뽑기 결과 반환 - 성공")
    void drawParts() throws Exception {

        givenLotteryApplierWhoDrawsPartsFirst();

        whenDrawParts();

        thenDrawParts();

        resultActions
                .andDo(document("event/parts/success",
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag(TAG_PARTS)
                                        .description("파츠 뽑기")
                                        .build()
                        )));
    }

    @Test
    @DisplayName("파츠 뽑기 결과 반환 - 횟수 소진")
    void drawPartsException() throws Exception {

        givenLotteryApplierWhoHasNoRemainChance();

        whenDrawParts();

        thenDrawPartsHasNotRemainChance();

        resultActions
                .andDo(document("event/parts/too-many-request",
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag(TAG_PARTS)
                                        .description("파츠 뽑기")
                                        .build()
                        )));
    }

    @Test
    @DisplayName("파츠 상태 변경 - 성공")
    void toggleParts() throws Exception {

        whenPartsEquippedStatusIsChanged(TEST_PARTS_ID);

        thenPartsEquippedStatusIsChanged();

        resultActions
                .andDo(document("event/parts/equip",
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag(TAG_PARTS)
                                        .description("자신의 파츠 상태 변경")
                                        .build()
                        )));
    }

    @Test
    @DisplayName("파츠 뽑기 횟수 반환 - 성공")
    void getRemainChance() throws Exception {

        givenLotteryApplierWhoHasRemainChance();

        whenMyRemainChanceIsRetrieved();

        thenMyRemainChanceIsRetrieved();

        resultActions
                .andDo(document("event/parts/remain",
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag(TAG_PARTS)
                                        .description("자신의 남은 파츠 뽑기 횟수 조회")
                                        .build()
                        )));
    }

    @Test
    @DisplayName("파츠 목록 반환 - 성공")
    void getMyPartsList() throws Exception {

        givenMyPartsList();

        whenMyPartsListAreRetrieved();

        thenMyPartsListAreRetrieved();

        resultActions
                .andDo(document("event/parts/get",
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag(TAG_PARTS)
                                        .description("자신의 파츠 목록 조회")
                                        .build()
                        )));
    }

    @Test
    @DisplayName("Uri 주인의 파츠 목록 반환 - 성공")
    void getLinkPartsList() throws Exception {

        givenPartsListForUri();

        whenPartsListAreRetrievedWithUri(TEST_URI);

        thenPartsListAreRetrievedWithUri();

        resultActions
                .andDo(document(DocumentConstants.PARTS_LINK_LIST,
                        resourceSnippetAuthed("description")));
    }

}