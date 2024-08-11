package com.watermelon.server.lottery.parts.controller;

import com.watermelon.server.ControllerTest;
import com.watermelon.server.DocumentConstants;
import com.watermelon.server.event.lottery.parts.controller.PartsController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.watermelon.server.Constants.TEST_PARTS_ID;
import static com.watermelon.server.Constants.TEST_URI;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
                        resourceSnippetAuthed("파츠 뽑기")));
    }

    @Test
    @DisplayName("파츠 뽑기 결과 반환 - 횟수 소진")
    void drawPartsException() throws Exception {

        givenLotteryApplierWhoHasNoRemainChance();

        whenDrawParts();

        thenDrawPartsHasNotRemainChance();

        resultActions
                .andDo(document("event/parts/too-many-request",
                        resourceSnippetAuthed("파츠 뽑기")));

    }

    @Test
    @DisplayName("파츠 상태 변경 - 성공")
    void toggleParts() throws Exception {

        whenPartsEquippedStatusIsChanged(TEST_PARTS_ID);

        thenPartsEquippedStatusIsChanged();

        resultActions
                .andDo(document("event/parts/equip",
                        resourceSnippetAuthed("자신의 파츠 상태 변경")));


    }

    @Test
    @DisplayName("파츠 뽑기 횟수 반환 - 성공")
    void getRemainChance() throws Exception {

        givenLotteryApplierWhoHasRemainChance();

        whenMyRemainChanceIsRetrieved();

        thenMyRemainChanceIsRetrieved();

        resultActions
                .andDo(document("event/parts/remain",
                        resourceSnippetAuthed("자신의 남은 파츠 뽑기 횟수 조회")));

    }

    @Test
    @DisplayName("파츠 목록 반환 - 성공")
    void getMyPartsList() throws Exception {

        givenMyPartsList();

        whenMyPartsListAreRetrieved();

        thenMyPartsListAreRetrieved();

        resultActions
                .andDo(document("event/parts/get",
                        resourceSnippetAuthed("자신의 파츠 목록 조회")))
                .andDo(print());

    }

    @Test
    @DisplayName("Uri 주인의 파츠 목록 반환 - 성공")
    void getLinkPartsList() throws Exception {

        givenPartsListForUri();

        whenPartsListAreRetrievedWithUri(TEST_URI);

        thenPartsListAreRetrievedWithUri();

        resultActions
                .andDo(document(DocumentConstants.PARTS_LINK_LIST,
                        resourceSnippetAuthed("링크 키의 주인에 대한 파츠 목록 조회")));

    }

}