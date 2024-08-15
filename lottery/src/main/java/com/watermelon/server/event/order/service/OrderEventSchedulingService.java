package com.watermelon.server.event.order.service;

import com.watermelon.server.event.order.domain.OrderEvent;
import com.watermelon.server.event.order.repository.OrderEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Repository;
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
    @CacheEvict("orderEvents")
    public void changeOrderStatusByTime(){
        List<OrderEvent> orderEvents = orderEventRepository.findAll();
        orderEvents.forEach(orderEvent -> {orderEvent.changeOrderEventStatusByTime(LocalDateTime.now());});
    }
    @Transactional
    public Long changeCurrentOrderEvent(){
        return orderEventCommandService.findOrderEventToMakeInProgress();
    }
}
