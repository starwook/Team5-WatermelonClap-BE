package com.watermelon.server.event.order.result.service;


import com.watermelon.server.event.order.repository.OrderEventWinnerRepository;
import com.watermelon.server.event.order.result.domain.OrderResult;
import com.watermelon.server.event.order.result.repository.OrderResultRepository;
import com.watermelon.server.token.ApplyTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderResultCommandService {
    private final OrderResultRepository orderResultRepository;
    private final OrderEventWinnerRepository orderEventWinnerRepository;
    private final ApplyTokenProvider applyTokenProvider;

    public OrderResult makeOrderEventApply(String applyToken){
        OrderResult orderResult = OrderResult.makeOrderEventApply(applyToken);
        return orderResultRepository.save(orderResult);
    }

}
