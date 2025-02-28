package com.watermelon.server.order.result.service;

import com.watermelon.server.order.domain.OrderEvent;
import com.watermelon.server.order.domain.OrderWinningCount;
import com.watermelon.server.order.dto.request.RequestOrderEventDto;
import com.watermelon.server.order.dto.request.RequestOrderRewardDto;
import com.watermelon.server.order.dto.request.RequestQuizDto;
import com.watermelon.server.order.repository.OrderApplyCountRepository;
import com.watermelon.server.order.repository.OrderEventRepository;

import com.watermelon.server.order.service.MemoryOrderEventService;
import com.watermelon.server.order.service.orderApply.OrderResultSaveService;
import com.watermelon.server.order.service.orderApply.OrderApplyService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;


@SpringBootTest
@DisplayName("[통합] 선착순 결과 Lock")
class OrderApplyResultLockTest {
    @Autowired
    private MemoryOrderEventService memoryOrderEventService;

    @Autowired
    private OrderApplyService orderApplyService;
    @Autowired
    private OrderEventRepository orderEventRepository;
    @Autowired
    private OrderApplyCountRepository orderApplyCountRepository;

    private OrderEvent orderEvent;

    private OrderWinningCount orderWinningCount;
    private OrderResultSaveService orderResultSaveService;

    @BeforeEach
    void setUp() {
         Optional<OrderWinningCount> applyCount = orderApplyCountRepository.findFirstApplyCountById();
         if(applyCount.isPresent()) {
             orderWinningCount = applyCount.get();
         }
         else {
             orderWinningCount = orderApplyCountRepository.save(OrderWinningCount.createWithNothing());
         }
         orderEvent = orderEventRepository.save(OrderEvent.makeOrderEventWithOutImage(
                RequestOrderEventDto.makeForTestOpened(
                        RequestQuizDto.makeForTest(), RequestOrderRewardDto.makeForTest()
                )
        ));
         memoryOrderEventService.refreshOrderEventInProgress(orderEvent);
    }
    @AfterEach
    void delete(){
        orderWinningCount.clearCount();
       orderEventRepository.delete(orderEvent);
    }
//    @Test
//    void 선착순_이벤트_락_적용_3배_신청() throws InterruptedException {
//        currentOrderEventManageService.clearOrderApplyCount();
//
//        int numberOfThreads = currentOrderEventManageService.getCurrentOrderEvent().getWinnerCount()*3;
//        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
//        CountDownLatch latch = new CountDownLatch(numberOfThreads);
//        for(int i=0;i<numberOfThreads;i++){
//            int finalI = i;
//            executorService.submit(()->{
//                try{
//                    orderResultCommandService.createTokenAndMakeTicket(orderEvent.getId());
//                }
//                finally {
//                    latch.countDown();
//                }
//            });
//        }
//        latch.await();
//        System.out.println("응모 당첨 개수: "+currentOrderEventManageService.getCurrentApplyCount());
//        Assertions.assertThat(currentOrderEventManageService.getCurrentApplyCount()).isEqualTo(currentOrderEventManageService.getCurrentOrderEvent().getWinnerCount());
//    }
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