package com.watermelon.server.order.result.repository;

import com.watermelon.server.config.MockLoginInterceptorConfig;
import com.watermelon.server.order.result.domain.OrderResult;
import com.watermelon.server.redis.config.EmbeddedRedisConfig;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.api.RSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;


@SpringBootTest(classes = EmbeddedRedisConfig.class)
@Import(MockLoginInterceptorConfig.class)
class OrderResultRepositoryTest {
    @Autowired
    private RSet<OrderResult> orderResults;

    @BeforeEach()
    void setUp() {
        orderResults.clear();
    }
    @Test
    @DisplayName("레디스 Repository OrderResult저장")
    public void saveReds(){
        OrderResult orderResult = OrderResult.makeOrderEventApply("testsToken");
        orderResults.add(orderResult);
        Assertions.assertThat(orderResults.contains(orderResult)).isTrue();
    }

}