package com.watermelon.server.order.service.orderApplyCount;

import com.watermelon.server.order.domain.OrderWinningCount;
import org.springframework.stereotype.Service;

@Service
public interface OrderEventWinningCountLockService {

    public OrderWinningCount getOrderApplyCountWithLock(long orderApplyCountId);

}
