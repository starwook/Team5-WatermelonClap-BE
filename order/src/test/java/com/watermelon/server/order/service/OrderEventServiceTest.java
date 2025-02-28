package com.watermelon.server.order.service;

import com.watermelon.server.OrderApplication;
import com.watermelon.server.order.domain.OrderEvent;
import com.watermelon.server.order.domain.OrderEventStatus;
import com.watermelon.server.order.dto.request.RequestOrderEventDto;
import com.watermelon.server.order.dto.request.RequestOrderRewardDto;
import com.watermelon.server.order.dto.request.RequestQuizDto;
import com.watermelon.server.order.exception.WrongOrderEventFormatException;
import com.watermelon.server.order.repository.OrderEventRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;


//@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) //replace None 사용시 실제 DB사용

@SpringBootTest(classes = OrderApplication.class)
@DisplayName("[단위] 선착순 서비스")
class OrderEventServiceTest {

    private static final Logger log = LoggerFactory.getLogger(OrderEventServiceTest.class);
    @Autowired
    private OrderEventQueryService orderEventQueryService;
    @Autowired
    private OrderEventCommandService orderEventCommandService;
    @Autowired
    private OrderEventRepository orderEventRepository;


    private OrderEvent orderEvent;
    @BeforeEach
    @DisplayName("선착순 이벤트 생성 - 성공")
    public void makeOrderEventsWithOutImageWithOutImage(){
        RequestQuizDto requestQuizDto = RequestQuizDto.makeForTest();
        RequestOrderRewardDto requestOrderRewardDto = RequestOrderRewardDto.makeForTest();
        RequestOrderEventDto requestOrderEventDto = RequestOrderEventDto.makeForTestOpen10HoursLater(requestQuizDto,requestOrderRewardDto);
        orderEvent = OrderEvent.makeOrderEventWithOutImage(requestOrderEventDto);
        orderEventRepository.save(orderEvent);
    }

    @Test
    @DisplayName("이벤트 존재 여부 조회 - 성공")
    @Order(1)
    public void checkEventExist(){
        Long eventId = orderEvent.getId();
        Assertions.assertThat(orderEventRepository.findById(eventId)).isPresent();
    }

    @Test
    @DisplayName("이벤트 존재 여부 조회 - 실패 (생성되지 않은 orderEventId)")
    @Order(2)
    public void checkOrderEventNotExist(){
        Long orderEventId = orderEvent.getId()+100;
        org.junit.jupiter.api.Assertions.assertThrows(WrongOrderEventFormatException.class, () -> {
            orderEventQueryService.getOrderEvent(orderEventId);
        });
    }

//    @Test
//    @DisplayName("선착순 이벤트 1초단위 상태변경 (UPCOMING->OPEN)")
//    @Order(2)
//    public void orderStatusChangeByTime() throws InterruptedException {
//        RequestQuizDto requestQuizDto =RequestQuizDto.makeForTest();
//        RequestOrderRewardDto requestOrderRewardDto = RequestOrderRewardDto.makeForTest();
//        RequestOrderEventDto requestOrderEventDto = RequestOrderEventDto.makeForTestOpenAfter1SecondCloseAfter3Second(requestQuizDto, requestOrderRewardDto);
//        OrderEvent newOrderEvent = OrderEvent.makeOrderEventWithOutImage(requestOrderEventDto);
//        orderEventRepository.save(newOrderEvent);
//        Assertions.assertThat(newOrderEvent.getOrderEventStatus()).isEqualTo(OrderEventStatus.UPCOMING);
//        Thread.sleep(2000L);
//        newOrderEvent = orderEventRepository.findById(newOrderEvent.getId()).get();
//        Assertions.assertThat(newOrderEvent.getOrderEventStatus()).isEqualTo(OrderEventStatus.OPEN);
//        Thread.sleep(2000L);
//        newOrderEvent = orderEventRepository.findById(newOrderEvent.getId()).get();
//        Assertions.assertThat(newOrderEvent.getOrderEventStatus()).isEqualTo(OrderEventStatus.END);
//    }

    @Test
    @DisplayName("상태 변경 - 성공")
    @Order(3)
    public void changeStatus(){
        List<OrderEvent> orderEvents = orderEventRepository.findAll();
        OrderEvent orderEvent = orderEvents.get(0);
        orderEvent.setOrderEventStatus(OrderEventStatus.CLOSED);
        Assertions.assertThat(orderEvent.getOrderEventStatus()).isEqualTo(OrderEventStatus.CLOSED);
    }

    @Test
    @DisplayName("전체 삭제 - 성공")
    public void deleteEventWithQuiz(){
        orderEventRepository.deleteAll();
        Assertions.assertThat(orderEventRepository.findAll().size()).isEqualTo(0);
    }



//    @Test
//    @DisplayName(" 상태 변경 확인(UPCOMING->OPEN)")
//    public void checkStatusChanged(){
//        List<OrderEvent> orderEvents = orderEventRepository.findAll();
//        Optional<OrderEvent> orderEvent = orderEvents
//                .stream()
//                .filter(o -> o.getOrderEventStatus().equals(OrderEventStatus.OPEN))
//                .findAny();
//        Assertions.assertThat(orderEvent.isPresent()).isTrue();
//    }


//    @Test
//    public void makeQuizTest(){
//        //then
//        Assertions.assertThat(orderEventRepository.findAll().size()).isEqualTo(1);
//        Assertions.assertThat(quizRepository.findAll().size()).isEqualTo(1);
//    }


}