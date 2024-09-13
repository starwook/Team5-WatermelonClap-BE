package com.watermelon.server.orderResult.service;

import com.watermelon.server.orderResult.domain.OrderApplyCount;
import com.watermelon.server.orderResult.repository.OrderApplyCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderApplyCountPessimisticLockLockService implements OrderApplyCountLockService {
    private final OrderApplyCountRepository orderApplyCountRepository;

    @Override
    public OrderApplyCount getOrderApplyCountWithLock(long orderApplyCountId) {
        return orderApplyCountRepository.findWithIdExclusiveLock(orderApplyCountId).get();
    }
}
