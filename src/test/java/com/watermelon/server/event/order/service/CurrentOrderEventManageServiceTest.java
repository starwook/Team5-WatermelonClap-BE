package com.watermelon.server.event.order.service;

import com.watermelon.server.event.order.result.domain.OrderResult;
import org.aspectj.weaver.ast.Or;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RSet;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrentOrderEventManageServiceTest {

    @Mock
    private RSet<String> applyTickets;
    @InjectMocks
    private CurrentOrderEventManageService currentOrderEventManageService;
    @BeforeEach
    void setUp() {
        currentOrderEventManageService.setMaxWinnerCount(100);
    }

    @Test
    @DisplayName("선착순 이벤트 제한수 확인")
    public void checkIsOrderApplyNotFullThenSave() {
        when(applyTickets.size()).thenReturn(0);
        Assertions.assertThat(currentOrderEventManageService.isOrderApplyNotFullThenSave(new OrderResult())).isTrue();
    }

    @Test
    @DisplayName("선착순 이벤트 제한수 확인(꽉참)")
    public void checkIsOrderApplyFull() {
        when(applyTickets.size()).thenReturn(currentOrderEventManageService.getMaxWinnerCount());
        Assertions.assertThat(currentOrderEventManageService.isOrderApplyNotFullThenSave(new OrderResult())).isFalse();

    }
}