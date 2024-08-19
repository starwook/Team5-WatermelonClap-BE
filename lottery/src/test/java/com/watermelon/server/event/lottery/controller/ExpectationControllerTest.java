package com.watermelon.server.event.lottery.controller;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.watermelon.server.ControllerTest;
import com.watermelon.server.event.lottery.domain.Expectation;
import com.watermelon.server.event.lottery.domain.LotteryApplier;
import com.watermelon.server.event.lottery.dto.request.RequestExpectationDto;
import com.watermelon.server.event.lottery.dto.response.ResponseExpectationDto;
import com.watermelon.server.event.lottery.error.ExpectationAlreadyExistError;
import com.watermelon.server.event.lottery.service.ExpectationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.watermelon.server.constants.Constants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ExpectationController.class)
class ExpectationControllerTest extends ControllerTest {

    @MockBean
    private ExpectationService expectationService;

    @Test
    @DisplayName("[DOC] 사용자 기대평을 만든다")
    void makeExpectation() throws Exception {
        final String PATH = "/expectations";
        final String DOCUMENT_NAME ="expectations/create";

        mvc.perform(
                RestDocumentationRequestBuilders.post(PATH)
                        .header(HEADER_NAME_AUTHORIZATION, HEADER_VALUE_BEARER + HEADER_VALUE_SPACE + TEST_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(RequestExpectationDto.makeExpectation("기개돼요")))
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(MockMvcRestDocumentationWrapper.document(DOCUMENT_NAME,
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag(TAG_EXPECTATION)
                                        .description("기대평 생성")
                                        .build()
                        )));

    }
    @Test
    @DisplayName("[DOC] 사용자 기대평을 만든다")
    void makeExpectationMakeConflictError() throws Exception {
        final String PATH = "/expectations";
        final String DOCUMENT_NAME ="expectation-conflict";
        Mockito.doThrow(new ExpectationAlreadyExistError()).when(expectationService).makeExpectation(any(),any());

        mvc.perform(
                        RestDocumentationRequestBuilders.post(PATH)
                                .header(HEADER_NAME_AUTHORIZATION, HEADER_VALUE_BEARER + HEADER_VALUE_SPACE + TEST_TOKEN)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(RequestExpectationDto.makeExpectation("기개돼요")))
                )
                .andExpect(status().isConflict())
                .andDo(print())
                .andDo(MockMvcRestDocumentationWrapper.document(DOCUMENT_NAME,
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag(TAG_EXPECTATION)
                                        .description("기대평 생성")
                                        .build()
                        )));

    }

    @Test
    @DisplayName("[DOC] 사용자 기대평을 가져온다")
    void getExpectationsForUser() throws Exception {
        final String PATH = "/expectations";
        final String DOCUMENT_NAME ="expectations";
        List<Expectation> expectations = new ArrayList<>();
        for(int i=0;i<10;i++){
            Expectation expectation = Expectation.makeExpectation(
                    RequestExpectationDto.makeExpectation("기대돼요"),
                    LotteryApplier.createLotteryApplier(String.valueOf(i))
            );
            expectation.toggleApproved();
            expectations.add(expectation);
        }
        List<ResponseExpectationDto> expectationDtoList = expectations.stream()
                        .map(expectation -> ResponseExpectationDto.forUser(expectation))
                .collect(Collectors.toList());

        Mockito.when(expectationService.getExpectationsForUser()).thenReturn(expectationDtoList);
        mvc.perform(RestDocumentationRequestBuilders.get(PATH))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(MockMvcRestDocumentationWrapper.document(DOCUMENT_NAME,
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag(TAG_EXPECTATION)
                                        .description("사용자 기대평 목록 조회")
                                        .build()
                        )));
    }

}