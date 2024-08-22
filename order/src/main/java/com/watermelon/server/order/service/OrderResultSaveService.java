package com.watermelon.server.order.service;

import com.watermelon.server.order.repository.OrderResultRepository;
import com.watermelon.server.order.result.domain.OrderResult;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderResultSaveService {
    private static final Logger log = LoggerFactory.getLogger(OrderResultSaveService.class);
    private final OrderResultRepository orderResultRepository;


    @Transactional
    public void saveOrderResult(OrderResult orderResult){
        orderResultRepository.save(orderResult);
    }
}
