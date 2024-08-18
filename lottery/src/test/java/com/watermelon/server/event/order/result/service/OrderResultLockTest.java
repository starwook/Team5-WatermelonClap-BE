package com.watermelon.server.event.order.result.service;

import com.watermelon.server.event.order.domain.OrderEvent;
import com.watermelon.server.event.order.dto.request.RequestOrderEventDto;
import com.watermelon.server.event.order.dto.request.RequestOrderRewardDto;
import com.watermelon.server.event.order.dto.request.RequestQuizDto;
import com.watermelon.server.event.order.result.domain.OrderResult;
import com.watermelon.server.event.order.service.CurrentOrderEventManageService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@SpringBootTest
class OrderResultLockTest {
    @Autowired
    private CurrentOrderEventManageService currentOrderEventManageService;

    @Autowired
    private OrderResultCommandService orderResultCommandService;

    @BeforeEach
    void setUp() {
       currentOrderEventManageService.clearOrderResultRepository();
       currentOrderEventManageService.refreshOrderEventInProgress(
               OrderEvent.makeOrderEventWithOutImage(
                       RequestOrderEventDto.makeForTestOpened(
                               RequestQuizDto.makeForTest(), RequestOrderRewardDto.makeForTest()
                       )
               )
       );
    }

    @Test
    void 선착순_이벤트_락_적용_25배_신청() throws InterruptedException {
        int numberOfThreads = currentOrderEventManageService.getCurrentOrderEvent().getWinnerCount()*25;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        for(int i=0;i<numberOfThreads;i++){
            int finalI = i;
            executorService.submit(()->{
                try{
                    orderResultCommandService.
                            saveOrderResultWithLock(OrderResult.makeOrderEventApply(String.valueOf(finalI)));
                }
                finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        System.out.println("OrderResult 개수: "+currentOrderEventManageService.getCurrentApplyTicketSize());
        Assertions.assertThat(currentOrderEventManageService.getCurrentApplyTicketSize()).isEqualTo(currentOrderEventManageService.getCurrentOrderEvent().getWinnerCount());
    }
//    @Test
//    void 선착순_이벤트_락_미적용_100명() throws InterruptedException {
//        int numberOfThreads = orderResultQueryService.getAvailableTicket()*10;
//        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
//        //스레드가 0이 될 떄까지 대기함.
//        CountDownLatch latch = new CountDownLatch(numberOfThreads);
//        for(int i=0;i<numberOfThreads;i++){
//            int finalI = i;
//            executorService.submit(()->{
//                try{
//                    orderResultCommandService
//                            .saveResponseResultWithOutLock(
//                                    OrderResult.makeOrderEventApply(String.valueOf(finalI)));
//                }
//                finally {
//                    latch.countDown(); //스레드 하나씩 다운
//                }
//            });
//        }
//        latch.await();// 스레드가 모두 끝날떄까지 대기
//        System.out.println("Lock 미적용 OrderResult 개수: "+orderResultSet.size());
//        Assertions.assertThat(orderResultSet.size()).isNotEqualTo(orderResultQueryService.getAvailableTicket());
//    }

}