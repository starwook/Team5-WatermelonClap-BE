package com.watermelon.server.admin.service;

import com.watermelon.server.order.domain.OrderEvent;
import com.watermelon.server.order.dto.request.RequestOrderEventDto;
import com.watermelon.server.order.dto.request.RequestOrderRewardDto;
import com.watermelon.server.order.dto.request.RequestQuizDto;
import com.watermelon.server.order.dto.response.ResponseOrderEventDto;
import com.watermelon.server.order.repository.OrderEventRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("[단위] 선착순 어드민 서비스")
class AdminOrderEventServiceTest {

    @InjectMocks
    private AdminOrderEventService adminOrderEventService;

    @Mock
    private OrderEventRepository orderEventRepository;

    private static List<OrderEvent> orderEvents = new ArrayList<>();

    private static OrderEvent orderEvent;
    @BeforeAll
    static void setUpBeforeClass(){
        RequestQuizDto requestQuizDto = RequestQuizDto.makeForTest();
        RequestOrderRewardDto requestOrderRewardDto = RequestOrderRewardDto.makeForTest();
        RequestOrderEventDto requestOrderEventDto = RequestOrderEventDto.makeForTestOpen10HoursLater(requestQuizDto,requestOrderRewardDto);
        orderEvent = OrderEvent.makeOrderEventWithOutImage(requestOrderEventDto);
        orderEvents.add(orderEvent);
        Assertions.assertThat(orderEvents.size()).isEqualTo(1);
    }

    @DisplayName("선착순 이벤트 조회 - 성공 (quiz answer 포함)")
    @Test
    void getOrderEvents() {
        when(orderEventRepository.findAll()).thenReturn(orderEvents);

        List<ResponseOrderEventDto> responseOrderEventDtos = adminOrderEventService.getOrderEventsForAdmin();
        responseOrderEventDtos.forEach( responseOrderEventDto ->
                Assertions.assertThat(responseOrderEventDto.getQuiz().getAnswer())
                        .isEqualTo(orderEvent.getQuiz().getAnswer())
        );
    }
}