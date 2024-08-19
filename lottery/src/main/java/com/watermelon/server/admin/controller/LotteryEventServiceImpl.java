package com.watermelon.server.admin.controller;

import com.watermelon.server.exception.S3ImageFormatException;
import com.watermelon.server.S3ImageService;
import com.watermelon.server.event.lottery.domain.LotteryEvent;
import com.watermelon.server.event.lottery.domain.LotteryReward;
import com.watermelon.server.event.lottery.dto.request.RequestLotteryEventDto;
import com.watermelon.server.event.lottery.repository.LotteryEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LotteryEventServiceImpl implements LotteryEventService{

    private final LotteryEventRepository lotteryEventRepository;

    private final S3ImageService imageService;

    @Override
    public void createLotteryEvent(RequestLotteryEventDto requestLotteryEventDto, List<MultipartFile> images) throws S3ImageFormatException {

        List<LotteryReward> lotteryRewards = new ArrayList<>();
        for(int i=0; i<requestLotteryEventDto.getRewards().size(); i++){
            RequestLotteryEventDto.RequestLotteryRewardDto requestLotteryRewardDto = requestLotteryEventDto.getRewards().get(i);
            LotteryReward lotteryReward = LotteryReward.createLotteryReward(
                    requestLotteryRewardDto.getRank(),
                    imageService.uploadImage(images.get(i)),
                    requestLotteryRewardDto.getName(),
                    requestLotteryRewardDto.getWinnerCount()
            );
            lotteryRewards.add(lotteryReward);
        }

        LotteryEvent lotteryEvent = LotteryEvent.create(
                requestLotteryEventDto.getName(),
                requestLotteryEventDto.getStartTime(),
                requestLotteryEventDto.getEndTime(),
                lotteryRewards
        );

        lotteryEventRepository.save(lotteryEvent);

    }
}
