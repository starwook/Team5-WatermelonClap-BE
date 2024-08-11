package com.watermelon.server.lottery.parts.controller;

import com.watermelon.server.ControllerTest;
import com.watermelon.server.DocumentConstants;
import com.watermelon.server.event.lottery.parts.controller.PartsController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
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
    @DisplayName("파츠 뽑기 결과 반환 - 횟수 소진")
    void drawPartsException() throws Exception {

        givenLotteryApplierWhoHasNoRemainChance();

        whenDrawParts();

        resultActions
                .andExpect(status().isTooManyRequests())
                .andDo(document("event/parts/too-many-request",
                        resourceSnippetAuthed("파츠 뽑기")));

    }

    @Test
    @DisplayName("파츠 상태 변경 - 성공")
    void toggleParts() throws Exception {

        whenPartsEquippedStatusIsChanged();

        resultActions
                .andExpect(status().isOk())
                .andDo(document("event/parts/equip",
                        resourceSnippetAuthed("자신의 파츠 상태 변경")));


    }

    @Test
    @DisplayName("파츠 뽑기 횟수 반환 - 성공")
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
    @DisplayName("파츠 목록 반환 - 성공")
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
    @DisplayName("Uri 주인의 파츠 목록 반환 - 성공")
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

}