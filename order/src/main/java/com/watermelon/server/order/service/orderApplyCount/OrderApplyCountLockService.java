package com.watermelon.server.order.service.orderApplyCount;

import com.watermelon.server.order.domain.OrderApplyCount;
import org.springframework.stereotype.Service;

@Service
public interface OrderApplyCountLockService {

    public OrderApplyCount getOrderApplyCountWithLock(long orderApplyCountId);

}
