package com.watermelon.server.orderApplyCount.service;

import com.watermelon.server.orderApplyCount.domain.OrderApplyCount;
import com.watermelon.server.orderApplyCount.repository.OrderApplyCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderApplyCountNamedLockService implements OrderApplyCountService{
    private final OrderApplyCountRepository orderApplyCountRepository;
    private final String LOCK_KEY ="ORDER_APPLY_COUNT";
    private final int WAIT_SECOND = 10;
    @Override
    @Transactional(transactionManager = "orderApplyCountTransactionManager")
    public boolean isOrderApplyCountAddable(long orderApplyCountId, int eachMaxWinnerCount) {
        try{
            orderApplyCountRepository.acquireNamedLock(LOCK_KEY,WAIT_SECOND);
            OrderApplyCount orderApplyCount = orderApplyCountRepository.findById(orderApplyCountId).get();
            return orderApplyCount.tryAddCountIfUnderMax(eachMaxWinnerCount);
        }
        finally {
            orderApplyCountRepository.releaseNamedLock(LOCK_KEY);
        }
    }
}
