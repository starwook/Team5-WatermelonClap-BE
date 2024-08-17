package com.watermelon.server.event.order.service;

import com.watermelon.server.common.cache.CacheService;
import com.watermelon.server.common.cache.CacheType;
import com.watermelon.server.event.order.domain.OrderEvent;
import com.watermelon.server.event.order.dto.response.ResponseOrderEventDto;
import com.watermelon.server.event.order.repository.OrderEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class OrderEventSchedulingService {

    private final OrderEventRepository orderEventRepository;
    private final OrderEventCommandService orderEventCommandService;
    private final CacheService cacheService;
    @Transactional
//    @CacheEvict(cacheNames = "orderEvents",allEntries = true)
    public void changeOrderStatusByTime(){
        List<OrderEvent> orderEvents = orderEventRepository.findAll();
        orderEvents.forEach(orderEvent -> {orderEvent.changeOrderEventStatusByTime(LocalDateTime.now());});

        List<ResponseOrderEventDto> newOrderEvents = orderEvents.stream()
                .map(ResponseOrderEventDto::forUser)
                .collect(Collectors.toList());
        cacheService.putCache(CacheType.ORDER_EVENTS.getCacheName(),
                cacheService.getOrderEventKey(),
                newOrderEvents);
    }

    @Transactional
    public Long changeCurrentOrderEvent(){
        return orderEventCommandService.findOrderEventToMakeInProgress();
    }
}
