package com.watermelon.server.order.service;

import com.watermelon.server.order.domain.OrderEvent;
import com.watermelon.server.order.dto.request.RequestOrderEventDto;
import com.watermelon.server.order.dto.request.RequestOrderRewardDto;
import com.watermelon.server.order.dto.request.RequestQuizDto;
import com.watermelon.server.orderResult.repository.OrderApplyCountRepository;
import com.watermelon.server.order.repository.OrderEventRepository;
import com.watermelon.server.orderResult.repository.OrderResultRepository;
import com.watermelon.server.orderResult.domain.OrderApplyCount;
import com.watermelon.server.orderResult.domain.OrderResult;
import com.watermelon.server.orderResult.service.CurrentOrderEventManageService;
import com.watermelon.server.orderResult.service.IndexLoadBalanceService;
import com.watermelon.server.orderResult.service.OrderApplyCountLockService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
//@TestPropertySource("classpath:application-local-db.yml")
@DisplayName("[단위] 선착순 관리 서비스")
class CurrentOrderEventManageServiceTest {

    @Mock
    private OrderEventRepository orderEventRepository;
    @Mock
    private OrderResultRepository orderResultRepository;
    @Mock
    private IndexLoadBalanceService indexLoadBalanceService;
    @InjectMocks
    private CurrentOrderEventManageService currentOrderEventManageService;
    @Mock
    private OrderApplyCountRepository orderApplyCountRepository;
    @Mock
    private OrderApplyCountLockService orderApplyCountLockService;
    private int applyCountIndex =1;
    @BeforeEach
    void setUp() {
//        when(orderApplyCountRepository.findFirstApplyCountById()).thenReturn(Optional.of(OrderApplyCount.createWithNothing()));
        currentOrderEventManageService.refreshOrderEventInProgress(
                OrderEvent.makeOrderEventWithOutImage(
                        RequestOrderEventDto.makeForTestOpened(
                                RequestQuizDto.makeForTest(), RequestOrderRewardDto.makeForTest()
                        )
                )
        );
        List<OrderApplyCount> orderApplyCountList =currentOrderEventManageService.getOrderApplyCountsFromServerMemory();
        for(int i=0;i<4;i++) orderApplyCountList.add(OrderApplyCount.createWithNothing());
    }

    @Test
    @DisplayName("선착순 이벤트 제한수 확인 - 성공")
    public void checkIsOrderApplyNotFullThenPlusCount() {
        OrderApplyCount orderApplyCount = currentOrderEventManageService.getOrderApplyCountsFromServerMemory().get(applyCountIndex);
        when(orderApplyCountLockService.getOrderApplyCountWithLock(anyLong())).thenReturn(orderApplyCount);
        Assertions.assertThat(currentOrderEventManageService.isOrderApplyNotFullThenPlusCount(0)).isTrue();
    }

    @Test
    @DisplayName("선착순 이벤트 제한수 확인 - 실패 (꽉참)")
    public void checkIsOrderApplyFull() {
        OrderApplyCount orderApplyCount = currentOrderEventManageService.getOrderApplyCountsFromServerMemory().get(applyCountIndex);
        for(int i=0;i<currentOrderEventManageService.getOrderEventFromServerMemory().getWinnerCount();i++) orderApplyCount.addCount();
        when(orderApplyCountLockService.getOrderApplyCountWithLock(anyLong())).thenReturn(orderApplyCount);
        Assertions.assertThat(currentOrderEventManageService.isOrderApplyNotFullThenPlusCount(0)).isFalse();
    }
}