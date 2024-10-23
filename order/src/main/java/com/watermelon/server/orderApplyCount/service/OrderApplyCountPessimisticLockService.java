package com.watermelon.server.orderApplyCount.service;

import com.watermelon.server.orderApplyCount.domain.OrderApplyCount;
import com.watermelon.server.orderApplyCount.repository.OrderApplyCountRepository;
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
