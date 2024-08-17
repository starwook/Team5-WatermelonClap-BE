package com.watermelon.server.redis.config;

import com.watermelon.server.order.result.domain.OrderResult;
import org.redisson.Redisson;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


@Profile("deploy")
@Configuration
public class RedissonConfig {
    private static final String REDISSON_HOST_PREFIX = "redis://";

    @Bean
    public RedissonClient redissonClient(@Value("${spring.data.redis.host}") String host,
                                         @Value("${spring.data.redis.port}") String port)
    {
        Config config = new Config();
        config.useSingleServer()
                .setAddress(REDISSON_HOST_PREFIX + host + ":" + port);

        return Redisson.create(config);

    }
    @Bean
    public RSet<OrderResult> orderResultSet(RedissonClient redissonClient) {
        return redissonClient.getSet("order-result");
    }
    @Bean
    public RSet<String> orderResultApplyTickets(RedissonClient redissonClient) {
        return redissonClient.getSet("order-result-apply-tickets");
    }

}
