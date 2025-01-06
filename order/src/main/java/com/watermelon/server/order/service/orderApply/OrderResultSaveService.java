package com.watermelon.server.order.service.orderApply;

import com.watermelon.server.order.domain.OrderApplyResult;
import com.watermelon.server.order.repository.OrderApplyResultRepository;
import com.watermelon.server.order.service.MemoryOrderEventService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.CannotCreateTransactionException;

@Service
@RequiredArgsConstructor
public class OrderResultSaveService {
    private static final Logger log = LoggerFactory.getLogger(OrderResultSaveService.class);
    private final OrderApplyResultRepository orderApplyResultRepository;
    private final MemoryOrderEventService memoryOrderEventService;



    public boolean isOrderApplyNotFullThenSaveConnectionOpen(String applyToken,int applyCountIndex) throws CannotCreateTransactionException {
        /**
         * 먼저 락을 걸고 ApplyCount의 숫자를 올리는 메소드가 성공하였다면
         * 토큰이 담긴 당첨 정보를 저장한다
         */
        if(memoryOrderEventService.isOrderApplyNotFullThenPlusCount(applyCountIndex)){
            OrderApplyResult orderApplyResult = OrderApplyResult.makeOrderEventApply(applyToken);
//            saveOrderResult(orderResult);
            return true;
        }
        return false;
    }
    public void saveOrderResult(OrderApplyResult orderApplyResult){
        orderApplyResultRepository.save(orderApplyResult);
    }
}
