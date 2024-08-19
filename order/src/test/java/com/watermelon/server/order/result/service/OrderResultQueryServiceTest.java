package com.watermelon.server.order.result.service;

import com.watermelon.server.order.result.domain.OrderResult;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RSet;

import static org.mockito.ArgumentMatchers.any;


@ExtendWith(MockitoExtension.class)
class OrderResultQueryServiceTest {


    @Mock
    private  RSet<OrderResult> orderResultSet;
    String applyToken= "applyToken";



}