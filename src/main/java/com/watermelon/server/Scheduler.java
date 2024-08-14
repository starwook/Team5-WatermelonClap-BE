package com.watermelon.server;

import com.watermelon.server.event.order.service.OrderEventSchedulingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class Scheduler {
    private final OrderEventSchedulingService orderEventSchedulingService;
    @Scheduled(fixedRate = 60000)
    public void checkOrderEvent(){
        log.info("Checking events by scheduled");
        orderEventSchedulingService.changeOrderStatusByTime();
        Long currentEventId = orderEventSchedulingService.changeCurrentOrderEvent();;
        log.info("current order event id is {}", currentEventId);
    }
}
