package com.watermelon.server.orderApplyCount.service;

import com.watermelon.server.orderApplyCount.domain.OrderApplyCount;
import org.springframework.stereotype.Service;

@Service
public interface OrderApplyCountLockService {

    public OrderApplyCount getOrderApplyCountWithLock(long orderApplyCountId);

}
