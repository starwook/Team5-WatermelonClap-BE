package com.watermelon.server.event.lottery.service;

import com.watermelon.server.admin.dto.response.ResponseLotteryApplierDto;
import com.watermelon.server.auth.service.AuthUserService;
import com.watermelon.server.event.lottery.domain.LotteryApplier;
import com.watermelon.server.event.lottery.domain.LotteryReward;
import com.watermelon.server.event.lottery.dto.response.ResponseLotteryRankDto;
import com.watermelon.server.event.lottery.exception.LotteryApplierNotFoundException;
import com.watermelon.server.event.lottery.repository.LotteryApplierRepository;
import com.watermelon.server.event.lottery.repository.LotteryRewardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LotteryServiceImpl implements LotteryService{

    private final LotteryApplierRepository lotteryApplierRepository;
    private final LotteryRewardRepository lotteryRewardRepository;
    private final AuthUserService authUserService;

    @Override
    public ResponseLotteryRankDto getLotteryRank(String uid) {
        return ResponseLotteryRankDto.from(
                findByUid(uid)
        );
    }

    @Override
    public LotteryApplier applyAndGet(String uid) {
        LotteryApplier applier = findByUid(uid);
        if(applier.isLotteryApplier()) return applier;
        applier.applyLottery();
        return lotteryApplierRepository.save(applier);
    }

    @Override
    public int getRemainChance(String uid) {
        return findByUid(uid).getRemainChance();
    }

    @Override
    public Page<ResponseLotteryApplierDto> getApplierInfoPage(Pageable pageable) {
        return lotteryApplierRepository.findByIsLotteryApplierTrue(pageable)
                .map(ResponseLotteryApplierDto::from);
    }

    @Transactional
    @Override
    public void lottery() {
        lotteryApplierRepository.initAllLotteryRank(-1);
        List<LotteryApplier> candidates = getLotteryCandidates();
        lotteryForCandidates(candidates);
    }

    private void lotteryForCandidates(List<LotteryApplier> candidates) {

        //참가자를 무작위로 섞는다.
        Collections.shuffle(candidates);

        //전체 보상 정보를 가져온다.
        List<LotteryReward> rewards = lotteryRewardRepository.findAll();

        //당첨자를 저장할 리스트
        List<LotteryApplier> lotteryWinners = new ArrayList<>();

        int all_count=0;
        int candidate_count=candidates.size();

        //당첨 정보를 설정하고, 당첨자 인원만큼 리스트에 담는다.
        for(LotteryReward reward : rewards){
            int winnerCount = reward.getWinnerCount();
            for(int i=0; i<winnerCount&&all_count<candidate_count; i++, all_count++){
                LotteryApplier winner = candidates.get(all_count);
                winner.lotteryWin(reward, authUserService.getUserEmail(winner.getUid()));
                lotteryWinners.add(winner);
            }
        }

        lotteryApplierRepository.saveAll(lotteryWinners);
    }

    private List<LotteryApplier> getLotteryCandidates() {
        return lotteryApplierRepository.findByIsLotteryApplierTrue();
    }

    @Override
    public LotteryApplier findLotteryApplierByUid(String uid){
        return findByUid(uid);
    }

    @Override
    public void registration(String uid) {
        log.info("registration uid: {}", uid);
        lotteryApplierRepository.save(LotteryApplier.createLotteryApplier(uid));
    }

    @Override
    public boolean isExist(String uid) {
        return lotteryApplierRepository.existsByUid(uid);
    }

    @Override
    @Transactional
    public void addRemainChance(String uid) {
        lotteryApplierRepository.addRemainChance(uid);
    }

    private LotteryApplier findByUid(String uid) {
        return lotteryApplierRepository.findByUid(uid).orElseThrow(LotteryApplierNotFoundException::new);
    }
}
