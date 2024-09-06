package com.watermelon.server.orderResult.service;

import org.springframework.stereotype.Service;



@Service
public class ConcurrentQueueIndexLoadBalanceService implements IndexLoadBalanceService {

    @Override
    public int getIndex() {
        return 0;
    }

    @Override
    public void addIndexToQueue(int totalIndexCount, int maxIndexNumber) {

    }

    @Override
    public void refreshQueue() {

    }

}
