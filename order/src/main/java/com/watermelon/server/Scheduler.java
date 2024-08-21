package com.watermelon.server;

import com.watermelon.server.order.service.CurrentOrderEventManageService;
import com.watermelon.server.order.service.OrderEventSchedulingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class Scheduler {
    private final OrderEventSchedulingService orderEventSchedulingService;
    private final CurrentOrderEventManageService currentOrderEventManageService;
    @Scheduled(fixedRate = 1000)
    public void checkOrderEvent(){
        Long currentEventId = currentOrderEventManageService.getCurrentOrderEventId();
        orderEventSchedulingService.changeOrderStatusByTime();
        Long newCurrentEventId = orderEventSchedulingService.changeCurrentOrderEvent();
        if(currentEventId!=null && !currentEventId.equals(newCurrentEventId)){
            log.info("changed current order event id is {}", newCurrentEventId);
        }
    }
    @Scheduled(fixedRate = 300000)
    public void checkCurrentOrderEvent(){
        Long currentEventId = currentOrderEventManageService.getCurrentOrderEventId();
        log.info("current order event id is {}", currentEventId);
    }
}
