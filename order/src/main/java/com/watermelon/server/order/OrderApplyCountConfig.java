package com.watermelon.server.order;

import com.watermelon.server.order.service.orderApplyCount.OrderApplyCountAsyncService;
import com.watermelon.server.order.service.orderApplyCount.OrderApplyCountLockService;
import com.watermelon.server.order.service.orderApplyCount.OrderApplyCountPessimisticLockService;
import com.watermelon.server.order.service.orderApplyCount.OrderApplyCountService;
import com.watermelon.server.order.repository.OrderApplyCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class OrderApplyCountConfig {
    private final OrderApplyCountRepository orderApplyCountRepository;

    @Bean
    public OrderApplyCountService orderApplyCountService(){
        return new OrderApplyCountAsyncService(orderApplyCountLockService());
    }

    @Bean
    public OrderApplyCountLockService orderApplyCountLockService(){
        return new OrderApplyCountPessimisticLockService(orderApplyCountRepository);
    }
}
