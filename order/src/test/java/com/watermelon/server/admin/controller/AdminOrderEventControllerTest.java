package com.watermelon.server.admin.controller;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.watermelon.server.ControllerTest;
import com.watermelon.server.admin.service.AdminOrderEventService;
import com.watermelon.server.order.domain.OrderEvent;
import com.watermelon.server.order.domain.OrderEventStatus;
import com.watermelon.server.order.domain.OrderEventWinner;
import com.watermelon.server.order.dto.request.OrderEventWinnerRequestDto;
import com.watermelon.server.order.dto.request.RequestOrderEventDto;
import com.watermelon.server.order.dto.request.RequestOrderRewardDto;
import com.watermelon.server.order.dto.request.RequestQuizDto;
import com.watermelon.server.order.dto.response.ResponseOrderEventDto;
import com.watermelon.server.order.dto.response.ResponseOrderEventWinnerDto;
import com.watermelon.server.order.exception.WrongOrderEventFormatException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.watermelon.server.constants.Constants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminOrderEventController.class)
@DisplayName("[단위] 선착순 어드민 컨트롤러")
class AdminOrderEventControllerTest extends ControllerTest {

    private final String TAG_ORDER ="선착순 이벤트";

    @MockBean
    private AdminOrderEventService adminOrderEventService;

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
        openOrderEvent = makeOrderEventIdWithDoc(1L);
        soonOpenOrderEvent = makeOrderEventIdWithDoc(2L);
        unOpenOrderEvent = makeOrderEventIdWithDoc(3L);
        openOrderEvent.setOrderEventStatus(OrderEventStatus.OPEN);

        orderEvents.add(openOrderEvent);
        orderEvents.add(soonOpenOrderEvent);
        orderEvents.add(unOpenOrderEvent);

        openOrderEventResponse = ResponseOrderEventDto.forAdmin(openOrderEvent);
        soonOpenResponse = ResponseOrderEventDto.forAdmin(soonOpenOrderEvent);
        unOpenResponse = ResponseOrderEventDto.forAdmin(unOpenOrderEvent);

        responseOrderEventDtos.add(openOrderEventResponse);
        responseOrderEventDtos.add(soonOpenResponse);
        responseOrderEventDtos.add(unOpenResponse);

    }
    private static OrderEvent makeOrderEventIdWithDoc(Long id) {
        return OrderEvent.makeOrderEventWithInputIdForDocumentation(
                RequestOrderEventDto.makeForTestOpened(
                        RequestQuizDto.makeForTest(),
                        RequestOrderRewardDto.makeForTest()
                ), id
        );
    }

    @Test
    @DisplayName("선착순 이벤트 당첨자 조회 - 성공")
    void getOrderEventWinners() throws Exception {
        final String PATH = "/admin/event/order/{eventId}/winner";
        final String DOCUMENT_NAME ="event-winner";

        List<OrderEventWinner>  winners = new ArrayList<>();
        for(int i=0;i<=10;i++){
            winners.add(
                    OrderEventWinner.makeWinner(openOrderEvent, OrderEventWinnerRequestDto.makeWithPhoneNumber(Integer.toString(i)),"answer","applyTicket")
            );
        }
        Mockito
                .when(adminOrderEventService.getOrderEventWinnersForAdmin(openOrderEvent.getId()))
                .thenReturn(winners.stream()
                        .map(orderEventWinner -> ResponseOrderEventWinnerDto.forAdmin(orderEventWinner))
                        .collect(Collectors.toList()));
        mvc.perform(
                        RestDocumentationRequestBuilders
                                .get(PATH,openOrderEvent.getId())
                                .header(HEADER_NAME_AUTHORIZATION, HEADER_VALUE_BEARER + " " + TEST_TOKEN)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(MockMvcRestDocumentationWrapper.document(DOCUMENT_NAME,
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag(TAG_ORDER)
                                        .description("어드민 선착순 이벤트 당첨자 반환")
                                        .build()
                        )));

    }

    @Test
    @DisplayName("어드민 선착순 이벤트 목록 조회 - 성공")
    void getOrderEvents() throws Exception {
        final String PATH = "/admin/event/order";
        final String DOCUMENT_NAME ="admin/event/order";
        Mockito.when(adminOrderEventService.getOrderEventsForAdmin()).thenReturn(responseOrderEventDtos);

        mvc.perform(RestDocumentationRequestBuilders.get(PATH)
                        .header(HEADER_NAME_AUTHORIZATION, HEADER_VALUE_BEARER + " " + TEST_TOKEN))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseOrderEventDtos)))
                .andDo(print())
                .andDo(MockMvcRestDocumentationWrapper.document(DOCUMENT_NAME,
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag(TAG_ORDER)
                                        .description("어드민 선착순 이벤트 목록 조회")
                                        .build()
                        )));
    }

    @Test
    @DisplayName("선착순 이벤트 생성 - 성공")
    void makeOrderEvent() throws Exception {
        final String PATH = "/admin/event/order";
        final String DOCUMENT_NAME ="admin/event/order/create";
        Mockito.when(adminOrderEventService.makeOrderEvent(any(),any(),any())).thenReturn(openOrderEventResponse);

        RequestOrderEventDto requestOrderEventDto = RequestOrderEventDto.makeForTestOpened(
                RequestQuizDto.makeForTest(),RequestOrderRewardDto.makeForTest()
        );
        MockMultipartFile rewardImage = new MockMultipartFile("rewardImage", "tooth.png", "multipart/form-data", "uploadFile".getBytes(StandardCharsets.UTF_8));
        MockMultipartFile quizImage  = new MockMultipartFile("quizImage", "tooth.png", "multipart/form-data", "uploadFile".getBytes(StandardCharsets.UTF_8));
        MockMultipartFile orderEvent  = new MockMultipartFile("orderEvent", "orderEvent", "application/json", objectMapper.writeValueAsString(requestOrderEventDto).getBytes(StandardCharsets.UTF_8));
        mvc.perform(
                        RestDocumentationRequestBuilders
                                .multipart(PATH)
                                .file(quizImage)
                                .file(rewardImage)
                                .file(orderEvent)
                                .header(HEADER_NAME_AUTHORIZATION, HEADER_VALUE_BEARER + " " + TEST_TOKEN)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(MockMvcRestDocumentationWrapper.document(DOCUMENT_NAME,
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag(TAG_ORDER)
                                        .description("어드민 선착순 이벤트 목록 조회")
                                        .build()
                        ),
                        requestParts(
                                partWithName("rewardImage").description("rewardImage"),
                                partWithName("quizImage").description("quizImage"),
                                partWithName("orderEvent").description("orderEvent")
                        )
                ));

    }

    @Test
    @DisplayName("선착순 이벤트 삭제 - 성공")
    void deleteOrderEvent() throws Exception {
        final String PATH = "/admin/event/order/{eventId}";
        final String DOCUMENT_NAME ="success";

        mvc.perform(RestDocumentationRequestBuilders.delete(PATH,1L)
                        .header(HEADER_NAME_AUTHORIZATION, HEADER_VALUE_BEARER + " " + TEST_TOKEN))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(MockMvcRestDocumentationWrapper.document(DOCUMENT_NAME,
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag(TAG_ORDER)
                                        .description("어드민 선착순 이벤트 삭제")
                                        .build()
                        )));

    }
    @Test
    @DisplayName("선착순 이벤트를 삭제 - 에러")
    void deleteOrderEventThrowError() throws Exception {
        final String PATH = "/admin/event/order/{eventId}";
        final String DOCUMENT_NAME ="not-exist";
        Mockito.doThrow(WrongOrderEventFormatException.class).when(adminOrderEventService).deleteOrderEvent(1L);

        mvc.perform(RestDocumentationRequestBuilders.delete(PATH,1L)
                        .header(HEADER_NAME_AUTHORIZATION, HEADER_VALUE_BEARER + " " + TEST_TOKEN))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andDo(MockMvcRestDocumentationWrapper.document(DOCUMENT_NAME,
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag(TAG_ORDER)
                                        .description("어드민 선착순 이벤트 삭제")
                                        .build()
                        )));

    }



}