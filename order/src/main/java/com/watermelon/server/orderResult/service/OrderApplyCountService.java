package com.watermelon.server.orderResult.service;

import org.springframework.stereotype.Service;

@Service
public interface OrderApplyCountService {

    public boolean isOrderApplyCountAddable(long orderApplyCountId,int eachMaxWinnerCount);
}
