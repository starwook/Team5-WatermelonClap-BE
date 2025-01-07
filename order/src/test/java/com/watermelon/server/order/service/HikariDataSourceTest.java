package com.watermelon.server.order.service;


import com.watermelon.server.order.service.orderApply.OrderResultSaveService;
import com.zaxxer.hikari.HikariDataSource;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@DisplayName("[통합] 커넥션 풀")
class HikariDataSourceTest {

    @Autowired
    private OrderResultSaveService orderResultSaveService;
    @Autowired
    private HikariDataSource originalHikariDataSource;


    @Test
    @DisplayName("분리된 커넥션 풀 확인")
    public void isCustomHikariDataSource() {
        Assertions.assertThat(originalHikariDataSource.getConnectionTimeout()).isNotEqualTo(originalHikariDataSource.getDataSource());

        /*ExecutorService executorService = Executors.newFixedThreadPool(maximumPoolSize);
        CountDownLatch latch = new CountDownLatch(maximumPoolSize);
        for(int i=0;i<maximumPoolSize;i++){//maximumpool만큼 잠든 후에
            int finalI = i;
            executorService.submit(()->{
                try{

                    orderResultSaveService.sleepWithTimeOut(timeOut*2); //두배만큼 쉬어준다
                } finally {
                    latch.countDown();

                }
            });
        }
        orderResultSaveService.isOrderApplyNotFullThenSaveConnectionOpen("applyToken");*/
    }
}