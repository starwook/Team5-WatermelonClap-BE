package com.watermelon.server.order.service.orderApplyCount;


import com.watermelon.server.order.domain.OrderWinningCount;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingDeque;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderEventWinningCountAsyncService implements OrderEventWinningCountService {
    private final OrderEventWinningCountLockService orderEventWinningCountLockService;
    public LinkedBlockingDeque<CompletableFuture<Boolean>> applyCountApis = new LinkedBlockingDeque<>();
    private long orderApplyCountId;
    private int eachMaxWinnerCount;

    @Override
    public boolean isOrderApplyCountAddable(long orderApplyCountId, int eachMaxWinnerCount) {
        this.orderApplyCountId = orderApplyCountId;
        this.eachMaxWinnerCount = eachMaxWinnerCount;
        return getEachResult().join();
    }

    public CompletableFuture<Boolean> getEachResult(){
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        applyCountApis.offer(future);
        return future;
    }


    @Scheduled(fixedRate = 250L)
    @Transactional(transactionManager = "orderApplyCountTransactionManager")
    public void processBatch(){
        if(orderApplyCountId == 0L) return;
        OrderWinningCount orderWinningCount = orderEventWinningCountLockService.getOrderApplyCountWithLock(orderApplyCountId);
        int remainCount = eachMaxWinnerCount- orderWinningCount.getCount();
        int plusCount;
        for(plusCount=0;plusCount<remainCount;plusCount++){
            if(applyCountApis.isEmpty()) break;
            CompletableFuture<Boolean> future = applyCountApis.poll();
            future.complete(true);
        }
        orderWinningCount.addCount(plusCount);
        while(!applyCountApis.isEmpty()){
            CompletableFuture<Boolean> future = applyCountApis.poll();
            future.complete(false);
        }
    }
}
