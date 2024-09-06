package com.watermelon.server.orderResult.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Service
public class BlockingQueueIndexLoadBalanceService implements IndexLoadBalanceService {
    public BlockingQueueIndexLoadBalanceService() {
        this.blockingQueue = new ArrayBlockingQueue<>(1000);
    }

    private BlockingQueue<Integer> blockingQueue;
    @Override
    public int getIndex() {
        return blockingQueue.poll();
    }
    @Override
    public void addIndexToQueue(int totalIndexCount, int maxIndexNumber) {
        for(int index=0;index<totalIndexCount;index++){
            blockingQueue.add((index%maxIndexNumber));
        }
    }
}

