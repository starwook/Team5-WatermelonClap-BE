package com.watermelon.server.event.order.result.service;

import com.watermelon.server.event.order.result.domain.OrderResult;
import org.assertj.core.api.Assertions;
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
class OrderResultQueryServiceTest {

    @Mock
    private OrderResultRepository orderResultRepository;

    @InjectMocks
    OrderResultQueryService orderResultQueryService;
    String applyToken= "applyToken";

    @Test
    @DisplayName("선착순 이벤트 제한수 확인")
    public void checkIsOrderApplyFull(){
        List<OrderResult> orderResults = new ArrayList<>();
        when(orderResultRepository.findAll()).thenReturn(orderResults);

        Assertions.assertThat(orderResultQueryService.isOrderApplyNotFull()).isTrue();
        for(int i=0;i<orderResultQueryService.getMaxCount();i++){
            orderResults.add(OrderResult.makeOrderEventApply(applyToken));
        }
        Assertions.assertThat(orderResultQueryService.isOrderApplyNotFull()).isFalse();
    }

}