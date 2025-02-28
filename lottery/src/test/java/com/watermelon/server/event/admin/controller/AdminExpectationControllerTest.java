package com.watermelon.server.event.admin.controller;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.watermelon.server.ControllerTest;
import com.watermelon.server.admin.controller.AdminExpectationController;
import com.watermelon.server.admin.dto.response.ResponseAdminExpectationApprovedDto;
import com.watermelon.server.event.lottery.domain.Expectation;
import com.watermelon.server.event.lottery.domain.LotteryApplier;
import com.watermelon.server.event.lottery.dto.request.RequestExpectationDto;
import com.watermelon.server.event.lottery.dto.response.ResponseExpectationDto;
import com.watermelon.server.event.lottery.service.ExpectationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.watermelon.server.auth.service.TestTokenVerifier.TEST_VALID_TOKEN;
import static com.watermelon.server.constants.Constants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(AdminExpectationController.class)
@DisplayName("[단위] 기대평 컨트롤러")
class AdminExpectationControllerTest extends ControllerTest {

    @Test
    @DisplayName("기대평 조회 - 성공")
    void getExpectationForAdmin() throws Exception {
        final String PATH ="/admin/expectations";
        final String DOCUMENT_NAME ="admin/expectations";
        List<ResponseExpectationDto> expectationDtoList = IntStream.range(0, 10)
                .mapToObj(i -> ResponseExpectationDto.forAdmin(
                        Expectation.makeExpectation(
                                RequestExpectationDto.makeExpectation("기대돼요"),
                                LotteryApplier.createLotteryApplier(String.valueOf(i))
                        )
                ))
                .collect(Collectors.toList());
        Mockito.when(expectationService.getExpectationsForAdmin()).thenReturn(expectationDtoList);

        mvc.perform(RestDocumentationRequestBuilders.get(PATH)
                        .header(HEADER_NAME_AUTHORIZATION, HEADER_VALUE_BEARER + " " + TEST_VALID_TOKEN))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(MockMvcRestDocumentationWrapper.document(DOCUMENT_NAME,
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag(TAG_EXPECTATION)
                                        .description("어드민 기대평 목록 조회")
                                        .build()
                        )));
    }
    @Test
    @DisplayName("기대평 상태 변경 - 성공")
    void toggleExpectation() throws Exception {
        final String PATH ="/admin/expectations/{expectationId}/toggle";
        final String DOCUMENT_NAME ="admin/expectations/{expectationId}/toggle";
        Expectation expectation = Expectation.makeExpectation(
                RequestExpectationDto.makeExpectation("기대돼요"),
                LotteryApplier.createLotteryApplier(String.valueOf(1))
        );
        Mockito.when(expectationService.toggleExpectation(any())).thenReturn(ResponseAdminExpectationApprovedDto.forAdminAfterToggleIsApproved(expectation));

        mvc.perform(RestDocumentationRequestBuilders.post(PATH,1)
                        .header(HEADER_NAME_AUTHORIZATION, HEADER_VALUE_BEARER + " " + TEST_VALID_TOKEN))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(MockMvcRestDocumentationWrapper.document(DOCUMENT_NAME,
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag(TAG_EXPECTATION)
                                        .description("기대평 상태 토글")
                                        .build()
                        )));
    }



}