package com.watermelon.server.orderApplyCount.service;


import com.watermelon.server.orderApplyCount.domain.OrderApplyCount;
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
        OrderApplyCount orderApplyCount = orderApplyCountLockService.getOrderApplyCountWithLock(orderApplyCountId);
        if(eachMaxWinnerCount>orderApplyCount.getCount()){
            orderApplyCount.addCountOnce();
            orderApplyCount.isCountMaxThenMakeFull(eachMaxWinnerCount);
            return true;
        }
        return false;
    }
}
