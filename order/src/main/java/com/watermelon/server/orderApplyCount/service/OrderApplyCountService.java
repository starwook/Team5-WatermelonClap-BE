package com.watermelon.server.orderApplyCount.service;

import org.springframework.stereotype.Service;

@Service
public interface OrderApplyCountService {

     boolean isOrderApplyCountAddable(long orderApplyCountId,int eachMaxWinnerCount);
}
