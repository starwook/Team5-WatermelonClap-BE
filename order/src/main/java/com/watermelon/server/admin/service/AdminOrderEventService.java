package com.watermelon.server.admin.service;


import com.watermelon.server.S3ImageService;
import com.watermelon.server.Scheduler;
import com.watermelon.server.exception.S3ImageFormatException;
import com.watermelon.server.order.domain.OrderEvent;
import com.watermelon.server.order.domain.OrderEventWinner;
import com.watermelon.server.order.dto.request.RequestOrderEventDto;
import com.watermelon.server.order.dto.response.ResponseOrderEventDto;
import com.watermelon.server.order.dto.response.ResponseOrderEventWinnerDto;
import com.watermelon.server.order.error.WrongOrderEventFormatException;
import com.watermelon.server.order.repository.OrderEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminOrderEventService {
    private final OrderEventRepository orderEventRepository;
    private final S3ImageService s3ImageService;

    private final Scheduler scheduler;

    @Transactional(readOnly = true)
    public List<ResponseOrderEventDto> getOrderEventsForAdmin() {
        List<OrderEvent> orderEvents = orderEventRepository.findAll();
        List<ResponseOrderEventDto> responseOrderEventDtos = new ArrayList<>();
        orderEvents.forEach(orderEvent -> responseOrderEventDtos.add(
                ResponseOrderEventDto.forAdmin(orderEvent)));
        return responseOrderEventDtos;
    }

    @Transactional(readOnly = true)
    public List<ResponseOrderEventWinnerDto> getOrderEventWinnersForAdmin(Long eventId) throws WrongOrderEventFormatException {
        OrderEvent orderEvent = orderEventRepository.findByIdFetchWinner(eventId).orElseThrow(WrongOrderEventFormatException::new);
        List<OrderEventWinner> orderEventWinners = orderEvent.getOrderEventWinner();
        return orderEventWinners.stream()
                .map(orderEventWinner -> ResponseOrderEventWinnerDto.forAdmin(orderEventWinner))
                .collect(Collectors.toList());
    }

    @Transactional
    public ResponseOrderEventDto makeOrderEvent(RequestOrderEventDto requestOrderEventDto, MultipartFile rewardImage, MultipartFile quizImage) throws S3ImageFormatException {
        String rewardImgSrc = s3ImageService.uploadImage(rewardImage);
        String quizImgSrc = s3ImageService.uploadImage(quizImage);
        OrderEvent newOrderEvent = OrderEvent.makeOrderEventWithImage(requestOrderEventDto,rewardImgSrc,quizImgSrc);
        saveOrderEvent(newOrderEvent);
        scheduler.checkOrderEvent();
        return ResponseOrderEventDto.forAdmin(newOrderEvent);
    }

    @CacheEvict(value = "orderEvents", allEntries = true)
    @Transactional
    public void saveOrderEvent(OrderEvent orderEvent){
        orderEventRepository.save(orderEvent);
    }

    @Transactional
    public void deleteOrderEvent(Long eventId) throws WrongOrderEventFormatException {
        OrderEvent orderEvent = orderEventRepository.findById(eventId).orElseThrow(WrongOrderEventFormatException::new);
        orderEventRepository.delete(orderEvent);
    }
}
