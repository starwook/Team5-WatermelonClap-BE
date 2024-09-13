package com.watermelon.server.orderResult.service;


import com.watermelon.server.orderResult.domain.OrderApplyCount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderApplyCountBasicService implements OrderApplyCountService {
    private final OrderApplyCountLockService orderApplyCountLockService;

    /**
     * Lock을 걸고 OrderApplyCount를 가져온다.
     * 그리고 인원을 더할 수 있다면 더한다.
     */
    @Override
    public boolean isOrderApplyCountAddable(long orderApplyCountId,int eachMaxWinnerCount) {
        OrderApplyCount orderApplyCount = orderApplyCountLockService.getOrderApplyCountWithLock(orderApplyCountId);
        if(eachMaxWinnerCount>orderApplyCount.getCount()){
            orderApplyCount.addCount();
            orderApplyCount.isCountMaxThenMakeFull(eachMaxWinnerCount);
            return true;
        }
        return false;
    }
}
