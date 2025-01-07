package com.watermelon.server.order.service.orderApplyCount;

import com.watermelon.server.order.domain.OrderWinningCount;
import com.watermelon.server.order.repository.OrderApplyCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderEventWinningCountPessimisticLockService implements OrderEventWinningCountLockService {
    private final OrderApplyCountRepository orderApplyCountRepository;

    @Override
    public OrderWinningCount getOrderApplyCountWithLock(long orderApplyCountId) {
        return orderApplyCountRepository.findWithIdExclusiveLock(orderApplyCountId).get();
    }
}
