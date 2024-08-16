package com.watermelon.server.event.parts.service;

import com.watermelon.server.event.lottery.domain.LotteryApplier;
import com.watermelon.server.event.parts.domain.LotteryApplierParts;
import com.watermelon.server.event.parts.domain.Parts;
import com.watermelon.server.event.parts.exception.PartsNotExistException;
import com.watermelon.server.event.parts.repository.LotteryApplierPartsRepository;
import com.watermelon.server.event.parts.repository.PartsRepository;
import com.watermelon.server.event.lottery.service.LotteryApplierService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LotteryApplierPartsServiceImpl implements LotteryApplierPartsService {

    private final LotteryApplierPartsRepository lotteryApplierPartsRepository;
    private final PartsRepository partsRepository;
    private final LotteryApplierService lotteryApplierService;

    @Override
    public LotteryApplierParts addPartsAndGet(LotteryApplier lotteryApplier, Parts parts) {

        boolean isFirst = isFirstPartsInCategory(lotteryApplier, parts);
        Optional<LotteryApplierParts> lotteryApplierPartsOptional = lotteryApplierPartsRepository
                .findLotteryApplierPartsByLotteryApplierUidAndPartsId(
                        lotteryApplier.getUid(), parts.getId()
                );

        LotteryApplierParts lotteryApplierParts;

        if (lotteryApplierPartsOptional.isEmpty()) {
            lotteryApplierParts = lotteryApplierPartsRepository.save(
                    LotteryApplierParts.createApplierParts(isFirst, lotteryApplier, parts)
            );
            //만약 모든 카테고리의 파츠를 모았다면
            if (hasAllCategoriesParts(lotteryApplier)) {
                //파츠 응모 처리 후 저장
                lotteryApplierService.applyPartsLotteryApplier(lotteryApplier);
            }
        } else lotteryApplierParts = lotteryApplierPartsOptional.get();

        return lotteryApplierParts;

    }

    boolean hasAllCategoriesParts(LotteryApplier lotteryApplier) {
        long partsCount = partsRepository.countCategoryDistinct();
        long lotteryApplierDistinctCount = lotteryApplierPartsRepository.countDistinctPartsCategoryByLotteryApplier(lotteryApplier);
        return lotteryApplierDistinctCount == partsCount;
    }

    @Transactional // 상태를 읽고 쓰는 과정에 원자성 보장 필요
    @Override
    public void toggleEquipped(String uid, Long partsId) {
        LotteryApplierParts lotteryApplierParts = lotteryApplierPartsRepository
                .findLotteryApplierPartsByLotteryApplierUidAndPartsId(uid, partsId).orElseThrow(PartsNotExistException::new);

        //상태 변경 전, 해당 카테고리의 장착된 파츠 해제
        Optional<LotteryApplierParts> existLAP = lotteryApplierPartsRepository
                .findEquippedLotteryApplierPartsByPartsCategoryAndLotteryApplier(
                        uid,
                        lotteryApplierParts.getParts().getCategory());

        if (existLAP.isPresent()&&!lotteryApplierParts.isEquipped()) {
            existLAP.get().toggleEquipped();
            lotteryApplierPartsRepository.save(existLAP.get());
        }

        lotteryApplierParts.toggleEquipped();

        lotteryApplierPartsRepository.save(lotteryApplierParts);
    }

    @Override
    public List<LotteryApplierParts> getListByApplier(String uid) {
        return lotteryApplierPartsRepository.findLotteryApplierPartsByLotteryApplierUid(uid);
    }

    /**
     * @param applier 응모자
     * @param parts   파츠
     * @return parts 카테고리의 파츠 첫 번째로 뽑았는지 여부
     */
    private boolean isFirstPartsInCategory(LotteryApplier applier, Parts parts) {
        return lotteryApplierPartsRepository.findLotteryApplierPartsByLotteryApplierId(applier.getId())
                .stream()
                .map(LotteryApplierParts::getParts)
                .noneMatch(parts1 -> parts1.getCategory().equals(parts.getCategory()));
    }

}
