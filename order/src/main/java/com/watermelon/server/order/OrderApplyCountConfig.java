package com.watermelon.server.order;

import com.watermelon.server.order.service.orderApplyCount.OrderEventWinningCountAsyncService;
import com.watermelon.server.order.service.orderApplyCount.OrderEventWinningCountLockService;
import com.watermelon.server.order.service.orderApplyCount.OrderEventWinningCountPessimisticLockService;
import com.watermelon.server.order.service.orderApplyCount.OrderEventWinningCountService;
import com.watermelon.server.order.repository.OrderApplyCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class OrderApplyCountConfig {
    private final OrderApplyCountRepository orderApplyCountRepository;

    @Bean
    public OrderEventWinningCountService orderApplyCountService(){
        return new OrderEventWinningCountAsyncService(orderApplyCountLockService());
    }

    @Bean
    public OrderEventWinningCountLockService orderApplyCountLockService(){
        return new OrderEventWinningCountPessimisticLockService(orderApplyCountRepository);
    }
}
