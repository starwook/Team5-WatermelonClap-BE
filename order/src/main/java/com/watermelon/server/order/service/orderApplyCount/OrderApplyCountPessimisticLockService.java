package com.watermelon.server.order.service.orderApplyCount;

import com.watermelon.server.order.domain.OrderApplyCount;
import com.watermelon.server.order.repository.OrderApplyCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderApplyCountPessimisticLockService implements OrderApplyCountLockService {
    private final OrderApplyCountRepository orderApplyCountRepository;

    @Override
    public OrderApplyCount getOrderApplyCountWithLock(long orderApplyCountId) {
        return orderApplyCountRepository.findWithIdExclusiveLock(orderApplyCountId).get();
    }
}
