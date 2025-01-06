package com.watermelon.server.order.service.orderApplyCount;


import com.watermelon.server.order.domain.OrderApplyCount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderApplyCountBasicService implements OrderApplyCountService {
    private final OrderApplyCountLockService orderApplyCountLockService;

    /**
     * Lock을 걸고 OrderApplyCount를 가져온다.
     * 그리고 인원을 더할 수 있다면 더한다.
     */
    @Override
    @Transactional(transactionManager = "orderApplyCountTransactionManager")
    public boolean isOrderApplyCountAddable(long orderApplyCountId,int eachMaxWinnerCount) {
        //락을 거는 곳
        OrderApplyCount orderApplyCount = orderApplyCountLockService.getOrderApplyCountWithLock(orderApplyCountId);
        return orderApplyCount.tryAddCountIfUnderMax(eachMaxWinnerCount);
    }
}
