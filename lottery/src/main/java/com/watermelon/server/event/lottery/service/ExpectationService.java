package com.watermelon.server.event.lottery.service;

import com.watermelon.server.admin.dto.response.ResponseAdminExpectationApprovedDto;
import com.watermelon.server.event.lottery.domain.Expectation;
import com.watermelon.server.event.lottery.domain.LotteryApplier;
import com.watermelon.server.event.lottery.dto.request.RequestExpectationDto;
import com.watermelon.server.event.lottery.dto.response.ResponseExpectationCheckDto;
import com.watermelon.server.event.lottery.dto.response.ResponseExpectationDto;
import com.watermelon.server.event.lottery.error.ExpectationAlreadyExistError;
import com.watermelon.server.event.lottery.error.ExpectationNotExist;
import com.watermelon.server.event.lottery.repository.ExpectationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpectationService {
    private final ExpectationRepository expectationRepository;
    private final LotteryService lotteryService;

    public void makeExpectation(String uid,RequestExpectationDto requestExpectationDto)
            throws ExpectationAlreadyExistError {
        LotteryApplier lotteryApplier = lotteryService.findLotteryApplierByUid(uid);
        if(isExpectationAlreadyExist(lotteryApplier)) throw new ExpectationAlreadyExistError();
        Expectation expectation = Expectation.makeExpectation(requestExpectationDto,lotteryApplier);
        saveExpectation(expectation);
    }
    public void saveExpectation(Expectation expectation) {
        expectationRepository.save(expectation);
    }

    public ResponseExpectationCheckDto isExpectationAlreadyExist(String uid) {
        return ResponseExpectationCheckDto.from(
                isExpectationAlreadyExist(lotteryService.findLotteryApplierByUid(uid))
        );
    }

    private static boolean isExpectationAlreadyExist(LotteryApplier lotteryApplier) {
        return lotteryApplier.getExpectation() != null;
    }


    @Cacheable(cacheNames = "expectations")
    public List<ResponseExpectationDto> getExpectationsForUser() {
        return expectationRepository.findTop30ByIsApprovedTrueOrderByCreatedAtDesc().stream()
                .map(expectation -> ResponseExpectationDto.forUser(expectation))
                .collect(Collectors.toList());
    }
    public List<ResponseExpectationDto> getExpectationsForAdmin() {
        return expectationRepository.findAll().stream()
                .map(expectation -> ResponseExpectationDto.forAdmin(expectation))
                .collect(Collectors.toList());
    }

    @Transactional
    @CacheEvict(cacheNames = "expectations",allEntries = true)
    public ResponseAdminExpectationApprovedDto toggleExpectation(Long expectationId) throws ExpectationNotExist {
        Expectation expectation = expectationRepository.findById(expectationId).orElseThrow(ExpectationNotExist::new);
        expectation.toggleApproved();
        return ResponseAdminExpectationApprovedDto.forAdminAfterToggleIsApproved(expectation);
    }
}
