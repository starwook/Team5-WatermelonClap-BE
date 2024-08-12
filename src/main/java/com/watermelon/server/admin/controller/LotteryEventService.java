package com.watermelon.server.admin.controller;

import com.watermelon.server.event.lottery.dto.request.RequestLotteryEventDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface LotteryEventService {

    void createLotteryEvent(RequestLotteryEventDto requestLotteryEventDto, List<MultipartFile> images);

}
