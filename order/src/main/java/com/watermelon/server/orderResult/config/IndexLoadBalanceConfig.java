package com.watermelon.server.orderResult.config;


import com.watermelon.server.orderResult.service.BlockingQueueIndexLoadBalanceService;
import com.watermelon.server.orderResult.service.IndexLoadBalanceService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;

@Configuration
public class IndexLoadBalanceConfig {
    @Bean
    public IndexLoadBalanceService indexLoadBalanceService() {
        return new BlockingQueueIndexLoadBalanceService();
    }
}
