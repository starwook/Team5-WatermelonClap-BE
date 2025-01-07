package com.watermelon.server.order.service.orderApplyCount;


import com.watermelon.server.order.domain.OrderWinningCount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderEventWinningCountBasicService implements OrderEventWinningCountService {
    private final OrderEventWinningCountLockService orderEventWinningCountLockService;
    /**
     * Lock을 걸고 OrderApplyCount를 가져온다.
     * 그리고 인원을 더할 수 있다면 더한다.
     */
    @Override
    @Transactional(transactionManager = "orderApplyCountTransactionManager")
    public boolean isOrderApplyCountAddable(long orderApplyCountId,int eachMaxWinnerCount) {
        //락을 거는 곳
        OrderWinningCount orderWinningCount = orderEventWinningCountLockService.getOrderApplyCountWithLock(orderApplyCountId);
        return orderWinningCount.tryAddCountIfUnderMax(eachMaxWinnerCount);
    }
}
