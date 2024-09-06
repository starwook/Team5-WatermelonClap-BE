package com.watermelon.server.orderResult.service;

import org.springframework.stereotype.Service;

@Service
public interface IndexLoadBalanceService {
    public int getIndex();
    public void addIndexToQueue(int totalIndexCount,int maxIndexNumber);
    public void refreshQueue();
}
