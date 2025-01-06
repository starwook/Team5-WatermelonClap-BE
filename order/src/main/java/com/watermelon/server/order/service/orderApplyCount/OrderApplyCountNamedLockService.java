package com.watermelon.server.order.service.orderApplyCount;

import com.watermelon.server.order.domain.OrderApplyCount;
import com.watermelon.server.order.repository.OrderApplyCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;


@Service
@RequiredArgsConstructor
public class OrderApplyCountNamedLockService implements OrderApplyCountService{
    private final OrderApplyCountRepository orderApplyCountRepository;
    private final String LOCK_KEY ="ORDER_APPLY_COUNT";
    private final int WAIT_SECOND = 10;
    @Override
    @Transactional(transactionManager = "orderApplyCountTransactionManager")
    public boolean isOrderApplyCountAddable(long orderApplyCountId, int eachMaxWinnerCount) {
        try {
            // 1. 네임드 락을 획득
            orderApplyCountRepository.acquireNamedLock(LOCK_KEY, WAIT_SECOND);

            // 2. 비즈니스 로직 수행
            OrderApplyCount orderApplyCount = orderApplyCountRepository.findById(orderApplyCountId).orElseThrow();
            boolean result = orderApplyCount.tryAddCountIfUnderMax(eachMaxWinnerCount);
            // 3. 트랜잭션 완료 후 락 해제를 등록
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCompletion(int status) {
                    // 트랜잭션 완료 후 락을 해제
                    orderApplyCountRepository.releaseNamedLock(LOCK_KEY);
                }
            });

            return result;
        } catch (Exception e) {
            // 예외 발생 시 즉시 락 해제
            orderApplyCountRepository.releaseNamedLock(LOCK_KEY);
            throw e;
        }
    }
}
