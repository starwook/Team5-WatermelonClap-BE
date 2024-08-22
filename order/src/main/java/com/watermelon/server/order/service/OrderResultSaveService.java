package com.watermelon.server.order.service;

import com.watermelon.server.order.repository.OrderResultRepository;
import com.watermelon.server.order.result.domain.OrderResult;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderResultSaveService {
    private static final Logger log = LoggerFactory.getLogger(OrderResultSaveService.class);
    private final OrderResultRepository orderResultRepository;
    private final CurrentOrderEventManageService currentOrderEventManageService;

    @Transactional
    public boolean isOrderApplyNotFullThenSaveConnectionOpen(String applyToken){
        if( currentOrderEventManageService.isOrderApplyNotFullThenPlusCount()){
            OrderResult orderResult = OrderResult.makeOrderEventApply(applyToken);
            saveOrderResult(orderResult);
            return true;
        }

        return false;
    }
    public void saveOrderResult(OrderResult orderResult){
        orderResultRepository.save(orderResult);
    }
}
