package com.watermelon.server.orderResult.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Service
public class BlockingQueueIndexLoadBalanceService implements IndexLoadBalanceService {


    private BlockingQueue<Integer> blockingQueue;
    public BlockingQueueIndexLoadBalanceService() {
        this.blockingQueue = new ArrayBlockingQueue<>(10000);
    }
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

    @Override
    public void refreshQueue() {
        this.blockingQueue.clear();

    }
}

