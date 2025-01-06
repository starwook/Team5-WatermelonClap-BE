package com.watermelon.server.orderResult.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Service
public class BlockingQueueTokenForDbAccessProviderService {


    private BlockingQueue<Integer> blockingQueue;
    public BlockingQueueTokenForDbAccessProviderService() {
        this.blockingQueue = new ArrayBlockingQueue<>(10000);
    }

    public int getIndex() {
        return blockingQueue.poll();
    }

    public void addIndexToQueue(int totalIndexCount, int maxIndexNumber) {
        for(int index=0;index<totalIndexCount;index++){
            blockingQueue.add((index%maxIndexNumber));
        }
    }

    public void refreshQueue() {
        this.blockingQueue.clear();
    }
}

