package com.watermelon.server.event.order.total;

import com.watermelon.server.admin.service.AdminOrderEventService;
import com.watermelon.server.common.cache.CacheService;
import com.watermelon.server.common.cache.CacheType;
import com.watermelon.server.event.order.domain.OrderEvent;
import com.watermelon.server.event.order.dto.request.RequestOrderEventDto;
import com.watermelon.server.event.order.dto.request.RequestOrderRewardDto;
import com.watermelon.server.event.order.dto.request.RequestQuizDto;
import com.watermelon.server.event.order.dto.response.ResponseOrderEventDto;
import com.watermelon.server.event.order.repository.OrderEventRepository;
import com.watermelon.server.event.order.service.OrderEventQueryService;
import com.watermelon.server.event.order.service.OrderEventSchedulingService;
import com.watermelon.server.integration.BaseIntegrationTest;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@DisplayName("[통합] 캐시 테스트 ")
@Slf4j
public class CacheTest extends BaseIntegrationTest {
    @Autowired
    private OrderEventRepository orderEventRepository;

    @Autowired
    private OrderEventQueryService orderEventQueryService;
    @Autowired
    private AdminOrderEventService adminOrderEventService;
    @Autowired
    private CacheService cacheService;
    @Autowired
    private OrderEventSchedulingService orderEventSchedulingService;

    @Test
    @DisplayName("캐시 적용 사이즈 동일")
    void checkCacheHit(){
        List<ResponseOrderEventDto> cachedOrderEvents = orderEventQueryService.getOrderEvents();
        orderEventRepository.save(OrderEvent.makeOrderEventWithOutImage(
                RequestOrderEventDto.makeForTestOpened(
                        RequestQuizDto.makeForTest(), RequestOrderRewardDto.makeForTest()
                )
        ));
        List<ResponseOrderEventDto> newCachedOrderEvents = orderEventQueryService.getOrderEvents();
        Assertions.assertThat(newCachedOrderEvents.size()).isEqualTo(cachedOrderEvents.size());
    }

    @Test
    @DisplayName("캐시 삭제 사이즈 +1")
    void checkCacheEvict(){
        List<ResponseOrderEventDto> cachedOrderEvents = orderEventQueryService.getOrderEvents();
        adminOrderEventService.saveOrderEventWithCacheEvict(OrderEvent.makeOrderEventWithOutImage(
                RequestOrderEventDto.makeForTestOpened(
                        RequestQuizDto.makeForTest(), RequestOrderRewardDto.makeForTest()
                )
        ));
        List<ResponseOrderEventDto> newCachedOrderEvents = orderEventQueryService.getOrderEvents();
        Assertions.assertThat(newCachedOrderEvents.size()).isEqualTo(cachedOrderEvents.size()+1);
    }


    @Test
    @DisplayName("캐시 GET BY CacheService 테스트")
    void getCache(){
        List<ResponseOrderEventDto> cachedOrderEvents = orderEventQueryService.getOrderEvents();
        Object object = cacheService.getCacheValueByObject(
                        CacheType.ORDER_EVENTS.getCacheName()
                        ,cacheService.getOrderEventKey()
                );
        Assertions.assertThat(object).isInstanceOf(List.class);
    }
    @Test
    @DisplayName("캐시 덮어씌우기 테스트")
    void putCache(){
        List<ResponseOrderEventDto> firstCached = orderEventQueryService.getOrderEvents();
        List<ResponseOrderEventDto> orderEventDtos = (List<ResponseOrderEventDto>) cacheService.getCacheValueByObject(
                CacheType.ORDER_EVENTS.getCacheName()
                ,cacheService.getOrderEventKey()
        );
        //cache를 Pull하기전은 다름
        int beforeSize = orderEventDtos.size();
        Assertions.assertThat(beforeSize).isEqualTo(firstCached.size());
        orderEventDtos.add(ResponseOrderEventDto.forUser(OrderEvent.makeOrderEventWithOutImage(
                RequestOrderEventDto.makeForTestOpened(
                        RequestQuizDto.makeForTest(), RequestOrderRewardDto.makeForTest()
                )
        )));

        cacheService.putCache(CacheType.ORDER_EVENTS.getCacheName(),cacheService.getOrderEventKey(),orderEventDtos);
        List<ResponseOrderEventDto> newOrderEventDtos = (List<ResponseOrderEventDto>) cacheService.getCacheValueByObject(
                CacheType.ORDER_EVENTS.getCacheName()
                ,cacheService.getOrderEventKey()
        );
        int afterSize = newOrderEventDtos.size();

        Assertions.assertThat(afterSize).isEqualTo(beforeSize+1);


    }

//    @Test
//    @DisplayName("Cache Expire Time 확인")
//    void checkCacheExpireTime() throws InterruptedException {
//        List<ResponseOrderEventDto> cachedOrderEvents = orderEventQueryService.getOrderEvents();
//        orderEventRepository.save(OrderEvent.makeOrderEventWithOutImage(
//                RequestOrderEventDto.makeForTestOpened(
//                        RequestQuizDto.makeForTest(), RequestOrderRewardDto.makeForTest()
//                )
//        ));
//        Thread.sleep(1000* CacheType.ORDER_EVENTS.getExpireTime());
//        List<ResponseOrderEventDto> newCachedOrderEvents = orderEventQueryService.getOrderEvents();
//        Assertions.assertThat(newCachedOrderEvents.size()).isEqualTo(cachedOrderEvents.size()+1);
//    }

}
