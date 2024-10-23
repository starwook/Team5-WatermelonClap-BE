package com.watermelon.server.orderApplyCount.config;

import com.watermelon.server.orderApplyCount.service.*;
import com.watermelon.server.orderApplyCount.repository.OrderApplyCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class OrderApplyCountConfig {
    private final OrderApplyCountRepository orderApplyCountRepository;

    @Bean
    public OrderApplyCountService orderApplyCountService(){
        return new OrderApplyCountNamedLockService(orderApplyCountRepository);
    }

    @Bean
    public OrderApplyCountLockService orderApplyCountLockService(){
        return new OrderApplyCountPessimisticLockService(orderApplyCountRepository);
    }
}
