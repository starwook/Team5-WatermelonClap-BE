package com.watermelon.server.order.service;

import com.watermelon.server.orderResult.repository.OrderResultRepository;
import com.watermelon.server.orderResult.domain.OrderResult;
import com.watermelon.server.orderResult.service.OrderEventFromServerMemoryService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderResultSaveService {
    private static final Logger log = LoggerFactory.getLogger(OrderResultSaveService.class);
    private final OrderResultRepository orderResultRepository;
    private final OrderEventFromServerMemoryService orderEventFromServerMemoryService;



    public boolean isOrderApplyNotFullThenSaveConnectionOpen(String applyToken,int applyCountIndex) throws CannotCreateTransactionException {
        /**
         * 먼저 락을 걸고 ApplyCount의 숫자를 올리는 메소드가 성공하였다면
         * 토큰이 담긴 당첨 정보를 저장한다
         */
        if(orderEventFromServerMemoryService.isOrderApplyNotFullThenPlusCount(applyCountIndex)){
            OrderResult orderResult = OrderResult.makeOrderEventApply(applyToken);
//            saveOrderResult(orderResult);
            return true;
        }
        return false;
    }
    public void saveOrderResult(OrderResult orderResult){
        orderResultRepository.save(orderResult);
    }
}
