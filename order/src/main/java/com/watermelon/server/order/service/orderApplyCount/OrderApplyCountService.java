package com.watermelon.server.order.service.orderApplyCount;

import org.springframework.stereotype.Service;

@Service
public interface OrderApplyCountService {

     boolean isOrderApplyCountAddable(long orderApplyCountId,int eachMaxWinnerCount);
}
