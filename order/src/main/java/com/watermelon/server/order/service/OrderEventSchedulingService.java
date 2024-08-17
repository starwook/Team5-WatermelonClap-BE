package com.watermelon.server.order.service;

import com.watermelon.server.order.domain.OrderEvent;
import com.watermelon.server.order.repository.OrderEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class OrderEventSchedulingService {

    private final OrderEventRepository orderEventRepository;
    private final OrderEventCommandService orderEventCommandService;

    @Transactional
    @CacheEvict(value = "orderEvents",allEntries = true)
    public void changeOrderStatusByTime(){
        List<OrderEvent> orderEvents = orderEventRepository.findAll();
        orderEvents.forEach(orderEvent -> {orderEvent.changeOrderEventStatusByTime(LocalDateTime.now());});
    }
    @Transactional
    public Long changeCurrentOrderEvent(){
        return orderEventCommandService.findOrderEventToMakeInProgress();
    }
}
