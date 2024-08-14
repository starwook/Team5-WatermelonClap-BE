package com.watermelon.server.event.order.result.service;

import com.watermelon.server.event.order.result.domain.OrderResult;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RSet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class OrderResultQueryServiceTest {


    @Mock
    private  RSet<OrderResult> orderResultSet;
    String applyToken= "applyToken";



}