package com.watermelon.server.order.controller;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippet;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.watermelon.server.ControllerTest;
import com.watermelon.server.order.exception.ApplyTicketWrongException;
import com.watermelon.server.order.domain.OrderEvent;
import com.watermelon.server.order.domain.OrderEventStatus;
import com.watermelon.server.order.dto.request.*;
import com.watermelon.server.order.dto.response.ResponseApplyTicketDto;
import com.watermelon.server.order.dto.response.ResponseOrderEventDto;
import com.watermelon.server.order.exception.NotDuringEventPeriodException;
import com.watermelon.server.order.exception.WinnerAlreadyParticipateException;
import com.watermelon.server.order.exception.WrongOrderEventFormatException;
import com.watermelon.server.order.exception.WrongPhoneNumberFormatException;
import com.watermelon.server.order.result.service.OrderResultCommandService;
import com.watermelon.server.order.service.OrderEventCommandService;
import com.watermelon.server.order.service.OrderEventQueryService;
import com.watermelon.server.order.controller.OrderEventController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderEventController.class)
@DisplayName("[단위] 선착순 컨트롤러")
class OrderEventControllerTest extends ControllerTest {
    private final String TAG_ORDER ="선착순 이벤트";
    @MockBean
    private OrderEventQueryService orderEventQueryService;
    @MockBean
    private OrderEventCommandService orderEventCommandService;

    @MockBean
    private OrderResultCommandService orderResultCommandService;



    private ResponseOrderEventDto openOrderEventResponse;
    private ResponseOrderEventDto soonOpenResponse;
    private ResponseOrderEventDto unOpenResponse;
    private OrderEvent soonOpenOrderEvent;
    private OrderEvent openOrderEvent;
    private OrderEvent unOpenOrderEvent;
    private List<OrderEvent> orderEvents = new ArrayList<>();
    private List<ResponseOrderEventDto> responseOrderEventDtos = new ArrayList<>();


    @BeforeEach
    void setUp(){
        openOrderEvent = OrderEvent.makeOrderEventWithInputIdForDocumentation(
                RequestOrderEventDto.makeForTestOpened(
                        RequestQuizDto.makeForTest(),
                        RequestOrderRewardDto.makeForTest()
                ),1L
        );
        soonOpenOrderEvent = OrderEvent.makeOrderEventWithInputIdForDocumentation(
                RequestOrderEventDto.makeForTestOpenAfter1SecondCloseAfter3Second
                        (
                                RequestQuizDto.makeForTest(),
                                RequestOrderRewardDto.makeForTest()
                        ),2L
        );
        unOpenOrderEvent = OrderEvent.makeOrderEventWithInputIdForDocumentation(
                RequestOrderEventDto.makeForTestOpen10HoursLater
                        (
                                RequestQuizDto.makeForTest(),
                                RequestOrderRewardDto.makeForTest()
                        ),3L
        );
        openOrderEvent.setOrderEventStatus(OrderEventStatus.OPEN);

        orderEvents.add(openOrderEvent);
        orderEvents.add(soonOpenOrderEvent);
        orderEvents.add(unOpenOrderEvent);

        openOrderEventResponse = ResponseOrderEventDto.forUser(openOrderEvent);
        soonOpenResponse = ResponseOrderEventDto.forUser(soonOpenOrderEvent);
        unOpenResponse = ResponseOrderEventDto.forUser(unOpenOrderEvent);

        responseOrderEventDtos.add(openOrderEventResponse);
        responseOrderEventDtos.add(soonOpenResponse);
        responseOrderEventDtos.add(unOpenResponse);

    }
    @Test
    @DisplayName("선착순 이벤트 목록 조회 - 성공")
    void getOrderEvents() throws Exception {
        final String PATH = "/event/order";
        final String DOCUMENT_NAME ="success";
        Mockito.when(orderEventQueryService.getOrderEvents()).thenReturn(responseOrderEventDtos);

        mvc.perform(RestDocumentationRequestBuilders.get(PATH))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseOrderEventDtos)))
                .andDo(print())
                .andDo(MockMvcRestDocumentationWrapper.document(DOCUMENT_NAME,
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag(TAG_ORDER)
                                        .description("선착순 이벤트 목록 조회")
                                        .build()
                        )));
    }
    @Test
    @DisplayName("선착순 이벤트 조회 - 성공")
    void getOrderEvent() throws Exception {
        final String PATH = "/event/order/{eventId}";
        final String DOCUMENT_NAME ="success}";
        Mockito.when(orderEventQueryService.getOrderEvent(openOrderEventResponse.getEventId())).thenReturn(openOrderEventResponse);

        mvc.perform(RestDocumentationRequestBuilders.get(PATH, openOrderEventResponse.getEventId()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(openOrderEventResponse)))
                .andDo(print())
                .andDo(MockMvcRestDocumentationWrapper.document(DOCUMENT_NAME,
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag(TAG_ORDER)
                                        .description("특정 선착순 이벤트 조회")
                                        .build()
                        )));
    }

    @Test
    @DisplayName("선착순 이벤트 조회 - 실패 (존재하지 않음)")
    void getOrderEventNotExist() throws Exception {
        final String PATH = "/event/order/{eventId}";
        final String DOCUMENT_NAME ="event-not-exist";
        Mockito.when(orderEventQueryService.getOrderEvent(openOrderEventResponse.getEventId())).thenThrow(WrongOrderEventFormatException.class);
        mvc.perform(RestDocumentationRequestBuilders.get(PATH, openOrderEventResponse.getEventId()))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andDo(MockMvcRestDocumentationWrapper.document(DOCUMENT_NAME,
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag(TAG_ORDER)
                                        .description("특정 선착순 이벤트 조회")
                                        .build()
                        )));
    }

    @Test
    @DisplayName("선착순 이벤트 번호 제출 - 성공 ")
    void makeApplyTicket() throws Exception {
        final String Path = "/event/order/{eventId}/{quizId}/apply";
        final String DOCUMENT_NAME ="success";


        String applyTicket = "applyTicket";

        mvc.perform(RestDocumentationRequestBuilders.post(Path,
                openOrderEventResponse.getEventId(),
                openOrderEventResponse.getQuiz().getQuizId())
                        .header("ApplyTicket",applyTicket)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(OrderEventWinnerRequestDto.makeWithPhoneNumber("01012341234")))

                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(MockMvcRestDocumentationWrapper.document(DOCUMENT_NAME,
                        orderNumberSubmitResource()));

//        Mockito.doThrow(WrongPhoneNumberFormatException.class).when(orderEventCommandService).makeOrderEventWinner(any(),any(),any());
//        mockMvc.perform(RestDocumentationRequestBuilders.post(Path,
//                                openOrderEventResponse.getEventId(),
//                                openOrderEventResponse.getQuiz().getQuizId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(OrderEventWinnerRequestDto.makeWithPhoneNumber("01012341234")))
//                        .header("ApplyTicket",applyTicket))
//                .andExpect(status().isUnprocessableEntity())
//                .andDo(print())
//                .andDo(MockMvcRestDocumentationWrapper.document(DOCUMENT_NAME,
//                        resourceSnippet("선착순 퀴즈 번호 제출")));
//
//        Mockito.doThrow(ApplyTicketWrongException.class).when(orderEventCommandService).makeOrderEventWinner(any(),any(),any());
//        mockMvc.perform(RestDocumentationRequestBuilders.post(Path,
//                                openOrderEventResponse.getEventId(),
//                                openOrderEventResponse.getQuiz().getQuizId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(OrderEventWinnerRequestDto.makeWithPhoneNumber("01012341234")))
//                        .header("ApplyTicket",applyTicket))
//                .andExpect(status().isUnauthorized())
//                .andDo(print())
//                .andDo(MockMvcRestDocumentationWrapper.document(DOCUMENT_NAME,
//                        resourceSnippet("선착순 퀴즈 번호 제출")));
    }
    @Test
    @DisplayName("선착순 이벤트 번호 제출 - 에러(apply 토큰 유효하지 않음)")
    void makeApplyTicketApplyTokenNotVerified() throws Exception {
        final String Path = "/event/order/{eventId}/{quizId}/apply";
        final String DOCUMENT_NAME = "apply-ticket-not-verified";
        String applyTicket = "applyTicket";
        Mockito.doThrow(ApplyTicketWrongException.class).when(orderEventCommandService).makeOrderEventWinner(any(),any(),any());


        mvc.perform(RestDocumentationRequestBuilders.post(Path,
                                openOrderEventResponse.getEventId(),
                                openOrderEventResponse.getQuiz().getQuizId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(OrderEventWinnerRequestDto.makeWithPhoneNumber("01012341234")))
                        .header("ApplyTicket", applyTicket))
                .andExpect(status().isUnauthorized())
                .andDo(print())
                .andDo(MockMvcRestDocumentationWrapper.document(DOCUMENT_NAME,
                        orderNumberSubmitResource()));
    }
    @Test
    @DisplayName("선착순 이벤트 번호 제출 - 에러(이미 참여함)")
    void makeApplyTicketAlreadyParticipate() throws Exception {
        final String Path = "/event/order/{eventId}/{quizId}/apply";
        final String DOCUMENT_NAME = "already-participate";
        String applyTicket = "applyTicket";
        Mockito.doThrow(WinnerAlreadyParticipateException.class).when(orderEventCommandService).makeOrderEventWinner(any(),any(),any());


        mvc.perform(RestDocumentationRequestBuilders.post(Path,
                                openOrderEventResponse.getEventId(),
                                openOrderEventResponse.getQuiz().getQuizId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(OrderEventWinnerRequestDto.makeWithPhoneNumber("01012341234")))
                        .header("ApplyTicket", applyTicket))
                .andExpect(status().isConflict())
                .andDo(print())
                .andDo(MockMvcRestDocumentationWrapper.document(DOCUMENT_NAME,
                        orderNumberSubmitResource()));
    }
    @Test
    @DisplayName("선착순 이벤트 번호 제출 - 에러(phone number 형식 맞지 않음)")
    void makeApplyTicketWrongPhoneNumberFormat() throws Exception {
        final String Path = "/event/order/{eventId}/{quizId}/apply";
        final String DOCUMENT_NAME = "phone-number-wrong-format";
        String applyTicket = "applyTicket";
        Mockito.doThrow(WrongPhoneNumberFormatException.class).when(orderEventCommandService).makeOrderEventWinner(any(),any(),any());


        mvc.perform(RestDocumentationRequestBuilders.post(Path,
                                openOrderEventResponse.getEventId(),
                                openOrderEventResponse.getQuiz().getQuizId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(OrderEventWinnerRequestDto.makeWithPhoneNumber("01012341234")))
                        .header("ApplyTicket", applyTicket))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
                .andDo(MockMvcRestDocumentationWrapper.document(DOCUMENT_NAME,
                        orderNumberSubmitResource()));

    }

    @Test
    @DisplayName("선착순 이벤트 번호 제출 - 에러(존재하지 않는 이벤트)")
    void makeApplyTicketWrongOrderEvent() throws Exception {
        final String Path = "/event/order/{eventId}/{quizId}/apply";
        final String DOCUMENT_NAME = "phone-number-wrong-format";
        String applyTicket = "applyTicket";
        Mockito.doThrow(WrongOrderEventFormatException.class).when(orderEventCommandService).makeOrderEventWinner(any(),any(),any());

        mvc.perform(RestDocumentationRequestBuilders.post(Path,
                                openOrderEventResponse.getEventId(),
                                openOrderEventResponse.getQuiz().getQuizId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(OrderEventWinnerRequestDto.makeWithPhoneNumber("01012341234")))
                        .header("ApplyTicket", applyTicket))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andDo(MockMvcRestDocumentationWrapper.document(DOCUMENT_NAME, orderNumberSubmitResource()));

    }
        @Test
    @DisplayName("선착순 이벤트 퀴즈 정답 제출 - 성공")
    void makeApply() throws Exception {
        final String Path = "/event/order/{eventId}/{quizId}";
        final String DOCUMENT_NAME ="success";
        String applyTicket = "applyTicket";
        Mockito.when(orderResultCommandService.makeApplyTicket(any(),any(),any())).thenReturn(ResponseApplyTicketDto.applySuccess(applyTicket));
        mvc.perform(RestDocumentationRequestBuilders.post(Path,
                                openOrderEventResponse.getEventId(),
                                openOrderEventResponse.getQuiz().getQuizId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(RequestAnswerDto.makeWith("answer")))
                         )
                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").value(objectMapper.writeValueAsString(ResponseApplyTicketDto.class)))
                .andDo(print())
                .andDo(MockMvcRestDocumentationWrapper.document(DOCUMENT_NAME,
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag(TAG_ORDER)
                                        .description("선착순 퀴즈 정답 제출")
                                        .build()
                        )));
    }
    @Test
    @DisplayName("선착순 이벤트 퀴즈 정답 제출 - 선착순 마감")
    void makeApplyClosed() throws Exception {
        final String Path = "/event/order/{eventId}/{quizId}";
        final String DOCUMENT_NAME ="full-apply";
        String applyTicket = "applyTicket";
        Mockito.when(orderResultCommandService.makeApplyTicket(any(),any(),any())).thenReturn(ResponseApplyTicketDto.fullApply());
        mvc.perform(RestDocumentationRequestBuilders.post(Path,
                                openOrderEventResponse.getEventId(),
                                openOrderEventResponse.getQuiz().getQuizId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(RequestAnswerDto.makeWith("answer")))
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(MockMvcRestDocumentationWrapper.document(DOCUMENT_NAME,
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag(TAG_ORDER)
                                        .description("선착순 퀴즈 정답 제출")
                                        .build()
                        )));
    }
    @Test
    @DisplayName("선착순 이벤트 퀴즈 정답 제출 - 정답 틀림")
    void makeApplyWrongAnswer() throws Exception {
        final String Path = "/event/order/{eventId}/{quizId}";
        final String DOCUMENT_NAME ="wrong-answer";
        String applyTicket = "applyTicket";
        Mockito.when(orderResultCommandService.makeApplyTicket(any(),any(),any())).thenReturn(ResponseApplyTicketDto.wrongAnswer());
        mvc.perform(RestDocumentationRequestBuilders.post(Path,
                                openOrderEventResponse.getEventId(),
                                openOrderEventResponse.getQuiz().getQuizId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(RequestAnswerDto.makeWith("answer")))
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(MockMvcRestDocumentationWrapper.document(DOCUMENT_NAME,
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag(TAG_ORDER)
                                        .description("선착순 퀴즈 정답 제출")
                                        .build()
                        )));
    }
    @Test
    @DisplayName("선착순 이벤트 퀴즈 정답 제출 -에러(기간이 틀림)")
    void makeApplyNotDuringDuration() throws Exception {
        final String Path = "/event/order/{eventId}/{quizId}";
        final String DOCUMENT_NAME ="not-during-duration";
        String applyTicket = "applyTicket";
        Mockito.when(orderResultCommandService.makeApplyTicket(any(),any(),any())).thenThrow(NotDuringEventPeriodException.class);
        mvc.perform(RestDocumentationRequestBuilders.post(Path,
                                openOrderEventResponse.getEventId(),
                                openOrderEventResponse.getQuiz().getQuizId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(RequestAnswerDto.makeWith("answer")))
                )
                .andExpect(status().isForbidden())
                .andDo(print())
                .andDo(MockMvcRestDocumentationWrapper.document(DOCUMENT_NAME,
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag(TAG_ORDER)
                                        .description("선착순 퀴즈 정답 제출")
                                        .build()
                        )));
    }
    @Test
    @DisplayName("선착순 이벤트 퀴즈 정답 제출 -에러(현재 진행중인 이벤트 아님)")
    void makeApplyWrongCurrentEvent() throws Exception {
        final String Path = "/event/order/{eventId}/{quizId}";
        final String DOCUMENT_NAME ="wrong-current-event";
        String applyTicket = "applyTicket";
        Mockito.when(orderResultCommandService.makeApplyTicket(any(),any(),any())).thenThrow(WrongOrderEventFormatException.class);
        mvc.perform(RestDocumentationRequestBuilders.post(Path,
                                openOrderEventResponse.getEventId(),
                                openOrderEventResponse.getQuiz().getQuizId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(RequestAnswerDto.makeWith("answer")))
                )
                .andExpect(status().isNotFound())
                .andDo(print())
                .andDo(MockMvcRestDocumentationWrapper.document(DOCUMENT_NAME,
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag(TAG_ORDER)
                                        .description("선착순 퀴즈 정답 제출")
                                        .build()
                        )));
    }

    private ResourceSnippet orderNumberSubmitResource(){
        return resource(
                ResourceSnippetParameters.builder()
                        .tag(TAG_ORDER)
                        .requestHeaders(
                                headerWithName("ApplyTicket").description("applyTicket")
                        )
                        .description("선착순 퀴즈 번호 제출")
                        .build()
        );
    }
}