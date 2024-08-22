package com.watermelon.server.event.lottery.service;

import com.watermelon.server.admin.dto.response.ResponseAdminLotteryWinnerDto;
import com.watermelon.server.event.lottery.domain.LotteryApplier;
import com.watermelon.server.event.lottery.domain.LotteryReward;
import com.watermelon.server.event.lottery.dto.request.RequestLotteryWinnerInfoDto;
import com.watermelon.server.event.lottery.dto.response.ResponseLotteryWinnerDto;
import com.watermelon.server.event.lottery.dto.response.ResponseLotteryWinnerInfoDto;
import com.watermelon.server.event.lottery.repository.LotteryApplierRepository;
import com.watermelon.server.event.lottery.repository.LotteryRewardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LotteryWinnerServiceImpl implements LotteryWinnerService{

    private final LotteryApplierRepository lotteryApplierRepository;
    private final LotteryRewardRepository lotteryRewardRepository;

    private final int NOT_RANKED = -1;

    @Override
    public List<ResponseLotteryWinnerDto> getLotteryWinners() {
        return lotteryApplierRepository.findByLotteryRankNot(NOT_RANKED).stream()
                .map(participant -> ResponseLotteryWinnerDto.from(
                        participant.getEmail(),
                        participant.getLotteryRank())
                )
                .collect(Collectors.toList());
    }

    @Override
    public ResponseLotteryWinnerInfoDto getLotteryWinnerInfo(String uid) {
        return ResponseLotteryWinnerInfoDto.from(
                lotteryApplierRepository.findByUid(uid).orElseThrow()
        );
    }

    @Override
    public void createLotteryWinnerInfo(String uid, RequestLotteryWinnerInfoDto requestLotteryWinnerInfoDto) {
        LotteryApplier lotteryApplier = lotteryApplierRepository.findByUid(uid).orElseThrow();
        lotteryApplier.setLotteryWinnerInfo(
                requestLotteryWinnerInfoDto.getAddress(),
                requestLotteryWinnerInfoDto.getName(),
                requestLotteryWinnerInfoDto.getPhoneNumber()
        );
        lotteryApplierRepository.save(lotteryApplier);
    }

    @Override
    @Transactional
    public List<ResponseAdminLotteryWinnerDto> getAdminLotteryWinners() {

        Map<Integer, LotteryReward> rankRewardMap = new HashMap<>();
        lotteryRewardRepository.findAll().forEach(r -> rankRewardMap.put(r.getLotteryRank(), r));

        return lotteryApplierRepository.findByLotteryRankNotOrderByLotteryRank(NOT_RANKED).stream()
                .map(l -> ResponseAdminLotteryWinnerDto.from(l, rankRewardMap.get(l.getLotteryRank())))
                .collect(Collectors.toList());
    }

    @Transactional //처음 상태와 변경 후 상태의 원자성 보장 필요.
    @Override
    public void lotteryWinnerCheck(String uid) {
        LotteryApplier lotteryApplier = lotteryApplierRepository.findByUid(uid).orElseThrow();
        lotteryApplier.lotteryWinnerCheck();
        lotteryApplierRepository.save(lotteryApplier);
    }


}
