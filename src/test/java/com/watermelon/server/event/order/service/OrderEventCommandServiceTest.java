package com.watermelon.server.event.order.service;

import com.watermelon.server.event.order.domain.OrderEvent;
import com.watermelon.server.event.order.domain.Quiz;
import com.watermelon.server.event.order.repository.OrderEventRepository;
import com.watermelon.server.event.order.result.service.OrderResultCommandService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;


@ExtendWith(MockitoExtension.class)
class OrderEventCommandServiceTest {

    @Mock
    private OrderEventRepository orderEventRepository;
    @Mock
    private CurrentOrderEventManageService currentOrderEventManageService;
    @Mock
    private OrderResultCommandService orderResultCommandService;
    @InjectMocks
    private OrderEventCommandService orderEventCommandService;


    private Long quizId=1L;
    private Long eventId =1L;
    private String answer ="answer";
    public LocalDateTime startDateTime;
    public LocalDateTime endDateTime;
    public String applyToken = "applyToken";
    OrderEvent orderEvent;

    @BeforeEach
    @DisplayName("응모 생성")
     void makeOrderEventWithOutImageWithOutImage(){
        LocalDateTime now = LocalDateTime.now();
        startDateTime = now;
        endDateTime = now.plusSeconds(10);
        orderEvent = OrderEvent.builder()
                .quiz(Quiz.builder().answer(answer).build())
                .startDate(startDateTime)
                .endDate(endDateTime)
                .build();
    }




}