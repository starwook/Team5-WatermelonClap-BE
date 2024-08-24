package com.watermelon.server.order.service;

import com.watermelon.server.orderResult.repository.OrderResultRepository;
import com.watermelon.server.orderResult.domain.OrderResult;
import com.watermelon.server.orderResult.service.CurrentOrderEventManageService;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderResultSaveService {
    private static final Logger log = LoggerFactory.getLogger(OrderResultSaveService.class);
    private final OrderResultRepository orderResultRepository;
    private final CurrentOrderEventManageService currentOrderEventManageService;

    @Qualifier("orderResultDatasource") //timeOut이 다른 커넥션을 가져온다.
    @Getter
    private final HikariDataSource dataSource;

    @Transactional(transactionManager = "orderResultTransactionManager") //transactional 매니저도 변경
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
