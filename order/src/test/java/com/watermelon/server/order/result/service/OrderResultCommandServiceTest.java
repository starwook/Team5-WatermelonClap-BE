package com.watermelon.server.order.result.service;

import com.watermelon.server.order.domain.ApplyTicketStatus;
import com.watermelon.server.order.dto.request.RequestAnswerDto;
import com.watermelon.server.order.exception.NotDuringEventPeriodException;
import com.watermelon.server.order.exception.WrongOrderEventFormatException;
import com.watermelon.server.order.result.domain.OrderResult;
import com.watermelon.server.order.service.CurrentOrderEventManageService;
import com.watermelon.server.token.ApplyTokenProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RSet;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderResultCommandServiceTest {


    @Mock
    private ApplyTokenProvider applyTokenProvider;

    @Mock
    private RSet<OrderResult> orderResultSet;
    @Mock
    private CurrentOrderEventManageService currentOrderEventManageService;
    @InjectMocks
    private OrderResultCommandService orderResultCommandService;

    private Long quizId=1L;
    private Long eventId =1L;
    private String answer ="answer";

    private String applyToken = "applyToken";

    @Test
    @DisplayName("선착순 응모 결과 생성(선착순 꽉차지 않을시))")
    public void makeOrderResult() {
        when(applyTokenProvider.createTokenByOrderEventId(any())).thenReturn(applyToken);

        when(currentOrderEventManageService.isOrderApplyNotFullThenSave(any())).thenReturn(true);
        Assertions.assertThat(orderResultCommandService.createTokenAndMakeTicket(1L).getResult())
                .isEqualTo(ApplyTicketStatus.SUCCESS.name());
    }
    @Test
    @DisplayName("선착순 응모 결과 생성(선착순 FULL)")
    public void makeOrderResultFull() {

        when(applyTokenProvider.createTokenByOrderEventId(any())).thenReturn(applyToken);
        when(currentOrderEventManageService.isOrderApplyNotFullThenSave(any())).thenReturn(false);
        Assertions.assertThat(orderResultCommandService.createTokenAndMakeTicket(1L).getResult())
                .isEqualTo(ApplyTicketStatus.CLOSED.name());
    }

    @Test
    @DisplayName("선착순 응모 - 정답 틀림 ")
    void makeApplyTicketWrongAnswer() throws NotDuringEventPeriodException, WrongOrderEventFormatException {
        when(currentOrderEventManageService.checkPrevious(any())).thenReturn(false);
        Assertions.assertThat(orderResultCommandService.makeApplyTicket(RequestAnswerDto.makeWith(answer),1L,1L).getResult())
                .isEqualTo(ApplyTicketStatus.WRONG.name());
    }


    @Test
    @DisplayName("선착순 응모 - 에러(존재하지 않음) ")
    void makeApplyTicketIdError() throws NotDuringEventPeriodException, WrongOrderEventFormatException {
        Mockito.doThrow(WrongOrderEventFormatException.class).when(currentOrderEventManageService).checkingInfoErrors(any(),any());
        Assertions.assertThatThrownBy(()->
                orderResultCommandService.makeApplyTicket(RequestAnswerDto.builder()
                        .answer(answer)
                        .build(),eventId,quizId)
        ).isInstanceOf(WrongOrderEventFormatException.class);
    }
    @Test
    @DisplayName("선착순 응모 - 에러(기간 오류)")
    void makeApplyTicketTimeError() throws NotDuringEventPeriodException, WrongOrderEventFormatException {
        Mockito.doThrow(NotDuringEventPeriodException.class).when(currentOrderEventManageService).checkingInfoErrors(any(),any());
        Assertions.assertThatThrownBy(()->
                orderResultCommandService.makeApplyTicket(RequestAnswerDto.builder()
                        .answer(answer)
                        .build(),eventId,quizId)
        ).isInstanceOf(NotDuringEventPeriodException.class);
    }

    @Test
    void makeApplyTicket() {
    }
}