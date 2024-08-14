package com.watermelon.server.admin.controller;

import com.watermelon.server.admin.exception.S3ImageFormatException;
import com.watermelon.server.event.lottery.dto.request.RequestLotteryEventDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface LotteryEventService {

    /**
     * 이미지와 정보를 받아서 S3에 업로드하고 저장한다.
     * @param requestLotteryEventDto 이미지 외 정보
     * @param images 이미지
     * @throws S3ImageFormatException
     */
    void createLotteryEvent(RequestLotteryEventDto requestLotteryEventDto, List<MultipartFile> images) throws S3ImageFormatException;

}
