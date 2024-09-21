package com.watermelon.server.order.service;

import com.watermelon.server.order.domain.OrderEvent;
import com.watermelon.server.order.dto.request.RequestOrderEventDto;
import com.watermelon.server.order.dto.request.RequestOrderRewardDto;
import com.watermelon.server.order.dto.request.RequestQuizDto;
import com.watermelon.server.orderApplyCount.repository.OrderApplyCountRepository;
import com.watermelon.server.order.repository.OrderEventRepository;
import com.watermelon.server.orderApplyCount.domain.OrderApplyCount;
import com.watermelon.server.orderApplyCount.service.OrderApplyCountService;
import com.watermelon.server.orderResult.service.OrderEventFromServerMemoryService;
import com.watermelon.server.orderResult.service.IndexLoadBalanceService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
//@TestPropertySource("classpath:application-local-db.yml")
@DisplayName("[단위] 선착순 관리 서비스")
class OrderEventFromServerMemoryServiceTest {

    @Mock
    private OrderEventRepository orderEventRepository;
    @Mock
    private IndexLoadBalanceService indexLoadBalanceService;
    @InjectMocks
    private OrderEventFromServerMemoryService orderEventFromServerMemoryService;
    @Mock
    private OrderApplyCountRepository orderApplyCountRepository;
    @Mock
    private OrderApplyCountService orderApplyCountService;

    private int applyCountIndex =1;
    @BeforeEach
    void setUp() {
        orderEventFromServerMemoryService.refreshOrderEventInProgress(
                OrderEvent.makeOrderEventWithOutImage(
                        RequestOrderEventDto.makeForTestOpened(
                                RequestQuizDto.makeForTest(), RequestOrderRewardDto.makeForTest()
                        )
                )
        );
        List<OrderApplyCount> orderApplyCountList = orderEventFromServerMemoryService.getOrderApplyCountsFromServerMemory();
        for(int i=0;i<4;i++){
            OrderApplyCount orderApplyCount = OrderApplyCount.createWithGeneratingId(i);
            orderApplyCountList.add(orderApplyCount);
        }
    }

    @Test
    @DisplayName("선착순 이벤트 제한수 확인 - 성공")
    public void checkIsOrderApplyNotFullThenPlusCount() {
        doReturn(true).when(orderApplyCountService).isOrderApplyCountAddable(anyLong(),anyInt());
        Assertions.assertThat(orderEventFromServerMemoryService.isOrderApplyNotFullThenPlusCount(0)).isTrue();
    }

    @Test
    @DisplayName("선착순 이벤트 제한수 확인 - 실패 (꽉참)")
    public void checkIsOrderApplyFull() {
        when(orderApplyCountService.isOrderApplyCountAddable(anyLong(),anyInt())).thenReturn(false);
        Assertions.assertThat(orderEventFromServerMemoryService.isOrderApplyNotFullThenPlusCount(0)).isFalse();
    }
}