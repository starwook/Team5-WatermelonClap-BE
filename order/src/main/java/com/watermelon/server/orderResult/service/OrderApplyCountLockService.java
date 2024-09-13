package com.watermelon.server.orderResult.service;

import com.watermelon.server.orderResult.domain.OrderApplyCount;
import org.springframework.stereotype.Service;

@Service
public interface OrderApplyCountLockService {

    public OrderApplyCount getOrderApplyCountWithLock(long orderApplyCountId);

}
