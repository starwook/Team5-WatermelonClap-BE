package com.watermelon.server.order.service;

import com.watermelon.server.common.cache.CacheService;
import com.watermelon.server.common.cache.CacheType;
import com.watermelon.server.order.domain.OrderEvent;
import com.watermelon.server.order.dto.response.ResponseOrderEventDto;
import com.watermelon.server.order.repository.OrderEventRepository;
import com.watermelon.server.order.service.OrderEventCommandService;
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
    // 선착순 이벤트의 상태를 시간에 따라 바꿔주는 메소드
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

    //현재 상태를 바꿔주고
    @Transactional
    public Long changeCurrentOrderEvent(){
        return orderEventCommandService.findOrderEventToMakeInProgress();
    }
}
