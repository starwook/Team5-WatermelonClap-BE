package com.watermelon.server.order.result.service;

import com.watermelon.server.order.domain.ApplyTicketStatus;
import com.watermelon.server.order.dto.request.RequestAnswerDto;
import com.watermelon.server.order.exception.NotDuringEventPeriodException;
import com.watermelon.server.order.exception.WrongOrderEventFormatException;
import com.watermelon.server.order.repository.OrderApplyResultRepository;
import com.watermelon.server.order.service.MemoryOrderEventService;
import com.watermelon.server.order.service.orderApply.OrderResultSaveService;
import com.watermelon.server.order.service.orderApply.OrderApplyService;
import com.watermelon.server.token.ApplyTokenProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;



import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("[단위] 선착순 결과 커맨드 서비스")
class OrderApplyResultCommandServiceTest {


    @Mock
    private ApplyTokenProvider applyTokenProvider;


    @Mock
    private MemoryOrderEventService memoryOrderEventService;
    @InjectMocks
    private OrderApplyService orderApplyService;
    @Mock
    private TokenForDbAccessProviderService tokenForDbAccessProviderService;
    @Mock
    private OrderApplyResultRepository orderApplyResultRepository;

    @Mock
    private OrderResultSaveService orderResultSaveService;


    private Long quizId=1L;
    private Long eventId =1L;
    private String answer ="answer";
    private int applyCountIndex =1;

    private String applyToken = "applyToken";

    @Test
    @DisplayName("선착순 응모 결과 생성(선착순 꽉차지 않을시))")
    public void makeOrderResult() {
        when(applyTokenProvider.createTokenByOrderEventId(any())).thenReturn(applyToken);
        when(tokenForDbAccessProviderService.getIndex()).thenReturn(applyCountIndex);
        when(orderResultSaveService.isOrderApplyNotFullThenSaveConnectionOpen(applyToken,applyCountIndex)).thenReturn(true);
        Assertions.assertThat(orderApplyService.createTokenAndMakeTicket(1L).getResult())
                .isEqualTo(ApplyTicketStatus.SUCCESS.name());
    }
    @Test
    @DisplayName("선착순 응모 결과 생성(선착순 FULL)")
    public void makeOrderResultFull() {

        when(applyTokenProvider.createTokenByOrderEventId(any())).thenReturn(applyToken);
        when(tokenForDbAccessProviderService.getIndex()).thenReturn(applyCountIndex);
        when(orderResultSaveService.isOrderApplyNotFullThenSaveConnectionOpen(applyToken,applyCountIndex)).thenReturn(false);
        Assertions.assertThat(orderApplyService.createTokenAndMakeTicket(1L).getResult())
                .isEqualTo(ApplyTicketStatus.CLOSED.name());
    }

    @Test
    @DisplayName("선착순 응모 - 정답 틀림 ")
    void makeApplyTicketWrongAnswer() throws NotDuringEventPeriodException, WrongOrderEventFormatException {
        when(memoryOrderEventService.checkPrevious(any())).thenReturn(false);
        Assertions.assertThat(orderApplyService.makeApplyTicket(RequestAnswerDto.makeWith(answer),1L,1L).getResult())
                .isEqualTo(ApplyTicketStatus.WRONG.name());
    }


    @Test
    @DisplayName("선착순 응모 - 에러(존재하지 않음) ")
    void makeApplyTicketIdError() throws NotDuringEventPeriodException, WrongOrderEventFormatException {
        Mockito.doThrow(WrongOrderEventFormatException.class).when(memoryOrderEventService).checkingInfoErrors(any(),any());
        Assertions.assertThatThrownBy(()->
                orderApplyService.makeApplyTicket(RequestAnswerDto.builder()
                        .answer(answer)
                        .build(),eventId,quizId)
        ).isInstanceOf(WrongOrderEventFormatException.class);
    }
    @Test
    @DisplayName("선착순 응모 - 에러(기간 오류)")
    void makeApplyTicketTimeError() throws NotDuringEventPeriodException, WrongOrderEventFormatException {
        Mockito.doThrow(NotDuringEventPeriodException.class).when(memoryOrderEventService).checkingInfoErrors(any(),any());
        Assertions.assertThatThrownBy(()->
                orderApplyService.makeApplyTicket(RequestAnswerDto.builder()
                        .answer(answer)
                        .build(),eventId,quizId)
        ).isInstanceOf(NotDuringEventPeriodException.class);
    }

    @Test
    void makeApplyTicket() {
    }
}