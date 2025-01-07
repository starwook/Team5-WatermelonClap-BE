package com.watermelon.server.order.total;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.watermelon.server.admin.service.AdminOrderEventService;
import com.watermelon.server.order.domain.*;
import com.watermelon.server.order.dto.request.*;
import com.watermelon.server.order.repository.OrderApplyCountRepository;
import com.watermelon.server.order.repository.OrderEventRepository;
import com.watermelon.server.order.domain.OrderWinningCount;
import com.watermelon.server.order.service.MemoryOrderEventService;
import com.watermelon.server.token.ApplyTokenProvider;
import com.watermelon.server.token.JwtPayload;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@DisplayName("[통합] 사용자 선착순 이벤트 ")
@Slf4j
@SpringBootTest
//@Disabled
@Transactional
@TestPropertySource("classpath:application-local-db.yml")
@AutoConfigureMockMvc
public class OrderEventTotalTest {

    @Autowired
    protected MockMvc mvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private OrderEventRepository orderEventRepository;
    @Autowired
    private AdminOrderEventService adminOrderEventService;
    @Autowired
    private MemoryOrderEventService memoryOrderEventService;


    @Autowired
    private ApplyTokenProvider applyTokenProvider;
    private OrderEvent soonOpenOrderEvent;
    private OrderEvent openOrderEvent;
    private OrderEvent unOpenOrderEvent;
    private OrderWinningCount orderWinningCount;
    @Autowired
    private OrderApplyCountRepository orderApplyCountRepository;


    @CacheEvict(value = "orderEvents",allEntries = true)
    @BeforeEach
    public void setUp(){
        orderWinningCount = OrderWinningCount.createWithNothing();
        orderApplyCountRepository.save(orderWinningCount);
        openOrderEvent = OrderEvent.makeOrderEventWithOutImage(
                RequestOrderEventDto.makeForTestOpened(
                        RequestQuizDto.makeForTest(),
                        RequestOrderRewardDto.makeForTest()
                )
        );
        openOrderEvent.setOrderEventStatus(OrderEventStatus.OPEN);
        soonOpenOrderEvent = OrderEvent.makeOrderEventWithOutImage(
                RequestOrderEventDto.makeForTestOpenAfter1SecondCloseAfter3Second
                                (
                        RequestQuizDto.makeForTest(),
                        RequestOrderRewardDto.makeForTest()
                                )
                );
        unOpenOrderEvent = OrderEvent.makeOrderEventWithOutImage(
                RequestOrderEventDto.makeForTestOpen10HoursLater
                        (
                                RequestQuizDto.makeForTest(),
                                RequestOrderRewardDto.makeForTest()
                        )
        );
        orderEventRepository.deleteAll();
    }
    @AfterEach
    public void tearDown(){
        orderApplyCountRepository.deleteAll();
    }
    @CacheEvict(value = "orderEvents",allEntries = true)
    @AfterEach
    public void deleteAll(){
        orderEventRepository.deleteAll();
    }

    @Test
    @DisplayName("선착순 이벤트 오픈된 이벤트 가져오기 - quiz = not exist")
    public void getOpenOrderEvent() throws Exception {
        adminOrderEventService.saveOrderEventWithCacheEvict(openOrderEvent);
        mvc.perform(get("/event/order"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].eventId").value(openOrderEvent.getId()))
                .andExpect(jsonPath("$[0].status").value(openOrderEvent.getOrderEventStatus().toString()))
                .andExpect(jsonPath("$[0].quiz").exists())
                .andDo(print());
    }

    @Test
    @DisplayName("선착순 이벤트 캐싱된지 확인 ")
    public void isOrderEventCached() throws Exception {
        adminOrderEventService.saveOrderEventWithCacheEvict(openOrderEvent);
        mvc.perform(get("/event/order"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andDo(print());
        adminOrderEventService.saveOrderEventWithCacheEvict(unOpenOrderEvent);
        mvc.perform(get("/event/order"))
                .andExpect(status().isOk())
                .andDo(print());
    }
    @Test
    @DisplayName("선착순 이벤트 퀴즈 - answer = null")
    public void getOpenOrderEventQuizAnswerNotExit() throws Exception {
        adminOrderEventService.saveOrderEventWithCacheEvict(openOrderEvent);

        mvc.perform(get("/event/order"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].quiz.answer").doesNotExist())
                .andDo(print());
    }
    @Test
    @DisplayName("선착순 이벤트 오픈 안 된 이벤트 가져오기 - quiz = null")
    public void getUnOpenOrderEvent() throws Exception {
        adminOrderEventService.saveOrderEventWithCacheEvict(unOpenOrderEvent);

        mvc.perform(get("/event/order"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].eventId").value(unOpenOrderEvent.getId()))
                .andExpect(jsonPath("$[0].status").value(unOpenOrderEvent.getOrderEventStatus().toString()))
                .andExpect(jsonPath("$[0].quiz").doesNotExist())
                .andDo(print());
    }

    @Test
    @DisplayName("존재하는 선착순 이벤트 가져오기")
    public void getExistOpenOrderEvent() throws Exception {
        adminOrderEventService.saveOrderEventWithCacheEvict(unOpenOrderEvent);
        mvc.perform(get("/event/order/{eventId}",unOpenOrderEvent.getId()))
                .andExpect(status().isOk())
                .andDo(print());
    }
    @Test
    @DisplayName("존재하는 선착순 이벤트 가져오기")
    public void getNotExistOpenOrderEvent() throws Exception {
        orderEventRepository.save(unOpenOrderEvent);
        mvc.perform(get("/event/order/{eventId}",unOpenOrderEvent.getId()+1))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("선착순 퀴즈 번호 제출 - 성공")
    public void orderEventApplyTicketNotWrong() throws Exception {
        adminOrderEventService.saveOrderEventWithCacheEvict(openOrderEvent);
        String applyTicket = applyTokenProvider.createTokenByOrderEventId(
                JwtPayload.from(String.valueOf(openOrderEvent.getId()))
        );

        OrderEventWinnerRequestDto emptyPhoneNumberDto =
                OrderEventWinnerRequestDto.makeWithPhoneNumber("01012341234");
        mvc.perform(post("/event/order/{eventId}/{quizId}/apply",openOrderEvent.getId(),1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emptyPhoneNumberDto))
                        .header("ApplyTicket",applyTicket))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("선착순 퀴즈 번호 제출-  전화 번호 형식 잘못됨 (에러)")
    public void orderEventApplyPhoneNumberFormatWrong() throws Exception {
        adminOrderEventService.saveOrderEventWithCacheEvict(openOrderEvent);
        OrderEventWinnerRequestDto emptyPhoneNumberDto =
                OrderEventWinnerRequestDto.makeWithPhoneNumber("");
        OrderEventWinnerRequestDto notStartWith010PhoneNumberDto =
                OrderEventWinnerRequestDto.makeWithPhoneNumber("23434343333");
        OrderEventWinnerRequestDto toLongPhoneNumberDto =
                OrderEventWinnerRequestDto.makeWithPhoneNumber("010232323435");
        mvc.perform(post("/event/order/{eventId}/{quizId}/apply",openOrderEvent.getId(),1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emptyPhoneNumberDto))
                        .header("ApplyTicket","ex"))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
        mvc.perform(post("/event/order/{eventId}/{quizId}/apply",openOrderEvent.getId(),1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(notStartWith010PhoneNumberDto))
                        .header("ApplyTicket","ex"))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
        mvc.perform(post("/event/order/{eventId}/{quizId}/apply",openOrderEvent.getId(),1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(toLongPhoneNumberDto))
                        .header("ApplyTicket","ex"))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }

    @Test
    @DisplayName("선착순 퀴즈 번호 제출 - ApplyTicket 형식 맞지 않음(다른 Claim key)")
    public void orderEventApplyTicketEventIdWrong() throws Exception {
        adminOrderEventService.saveOrderEventWithCacheEvict(openOrderEvent);
        String applyTicket = applyTokenProvider.createTokenByOrderEventId(
                JwtPayload.from(String.valueOf(openOrderEvent.getId()+1))
        );

        OrderEventWinnerRequestDto emptyPhoneNumberDto =
                OrderEventWinnerRequestDto.makeWithPhoneNumber("01012341234");
        mvc.perform(post("/event/order/{eventId}/{quizId}/apply",openOrderEvent.getId(),1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emptyPhoneNumberDto))
                        .header("ApplyTicket",applyTicket))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }
    @Test
    @DisplayName("선착순 퀴즈 번호 제출 - 이미 참여함)")
    public void orderEventApplyTicketAlreadyParticipate() throws Exception {
        adminOrderEventService.saveOrderEventWithCacheEvict(openOrderEvent);
        String applyTicket = applyTokenProvider.createTokenByOrderEventId(
                JwtPayload.from(String.valueOf(openOrderEvent.getId()))
        );

        OrderEventWinnerRequestDto emptyPhoneNumberDto =
                OrderEventWinnerRequestDto.makeWithPhoneNumber("01012341234");
        mvc.perform(post("/event/order/{eventId}/{quizId}/apply",openOrderEvent.getId(),1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emptyPhoneNumberDto))
                        .header("ApplyTicket",applyTicket))
                .andExpect(status().isOk())
                .andDo(print());

        //두번째엔 에러 발생
        mvc.perform(post("/event/order/{eventId}/{quizId}/apply",openOrderEvent.getId(),1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emptyPhoneNumberDto))
                        .header("ApplyTicket",applyTicket))
                .andExpect(status().isConflict())
                .andDo(print());
    }

//    @Test
//    @DisplayName("선착순 퀴즈 제출 - 성공")
//    public void orderEventApply() throws Exception {
//        adminOrderEventService.saveOrderEventWithCacheEvict(openOrderEvent);
//        currentOrderEventManageService.refreshOrderEventInProgress(openOrderEvent);
//        Quiz quiz = openOrderEvent.getQuiz();
//        RequestAnswerDto requestAnswerDto = RequestAnswerDto.makeWith(quiz.getAnswer());
//        mvc.perform(post("/event/order/{eventId}/{quizId}",openOrderEvent.getId(),quiz.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(requestAnswerDto)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.result").value(ApplyTicketStatus.SUCCESS.toString()))
//                .andExpect(jsonPath("$.applyTicket").exists())
//                .andDo(print());
//    }

    @Test
    @DisplayName("선착순 퀴즈 제출 - 실패(에러 - 현재 진행되지 않는 이벤트,퀴즈 ID)")
    public void orderEventApplyWrongEventId() throws Exception {
        adminOrderEventService.saveOrderEventWithCacheEvict(openOrderEvent);
        memoryOrderEventService.refreshOrderEventInProgress(openOrderEvent);
        Quiz quiz = openOrderEvent.getQuiz();
        RequestAnswerDto requestAnswerDto = RequestAnswerDto.makeWith(quiz.getAnswer());
        mvc.perform(post("/event/order/{eventId}/{quizId}",openOrderEvent.getId()+1L,quiz.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestAnswerDto)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("선착순 퀴즈 제출 - 실패(에러 - 기간이 틀림)")
    public void orderEventApplyWrongDuration() throws Exception {
        adminOrderEventService.saveOrderEventWithCacheEvict(unOpenOrderEvent);
        memoryOrderEventService.refreshOrderEventInProgress(unOpenOrderEvent);
        Quiz quiz = unOpenOrderEvent.getQuiz();
        RequestAnswerDto requestAnswerDto = RequestAnswerDto.makeWith(quiz.getAnswer());
        mvc.perform(post("/event/order/{eventId}/{quizId}",unOpenOrderEvent.getId(),quiz.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestAnswerDto)))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    @DisplayName("선착순 퀴즈 제출 - 실패(정답이 틀림)")
    public void orderEventApplyWrongAnswer() throws Exception {
        adminOrderEventService.saveOrderEventWithCacheEvict(openOrderEvent);
        memoryOrderEventService.refreshOrderEventInProgress(openOrderEvent);
        Quiz quiz = openOrderEvent.getQuiz();
        RequestAnswerDto requestAnswerDto = RequestAnswerDto.makeWith(quiz.getAnswer()+"/wrong");
        mvc.perform(post("/event/order/{eventId}/{quizId}",openOrderEvent.getId(),quiz.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestAnswerDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(ApplyTicketStatus.WRONG.toString()))
                .andExpect(jsonPath("$.applyTicket").doesNotExist())
                .andDo(print());
    }

//    @Test
//    @DisplayName("선착순 퀴즈 제출 - 실패(선착순 마감)")
//    public void orderEventApplyClosed() throws Exception {
//        adminOrderEventService.saveOrderEventWithCacheEvict(openOrderEvent);
//        currentOrderEventManageService.refreshOrderEventInProgress(openOrderEvent);
//
//        Quiz quiz = openOrderEvent.getQuiz();
//        RequestAnswerDto requestAnswerDto = RequestAnswerDto.makeWith(quiz.getAnswer());
//
//        /**
//         * 선착순 최대 인원 수만큼 응모 추가
//         */
//        for(int i=0;i<currentOrderEventManageService.getCurrentOrderEvent().getWinnerCount();i++){
//            mvc.perform(post("/event/order/{eventId}/{quizId}",openOrderEvent.getId(),quiz.getId())
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(requestAnswerDto)))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.result").value(ApplyTicketStatus.SUCCESS.toString()))
//                    .andExpect(jsonPath("$.applyTicket").exists());
//        }
//
//
//        Assertions.assertThat(currentOrderEventManageService.getCurrentApplyCount()).isEqualTo(100);
//        mvc.perform(post("/event/order/{eventId}/{quizId}",openOrderEvent.getId(),quiz.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(requestAnswerDto)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.result").value(ApplyTicketStatus.CLOSED.toString()))
//                .andExpect(jsonPath("$.applyTicket").doesNotExist())
//                .andDo(print());
//    }

}
