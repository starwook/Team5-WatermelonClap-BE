package com.watermelon.server.event.parts.service;

import com.watermelon.server.event.lottery.domain.LotteryApplier;
import com.watermelon.server.event.parts.domain.LotteryApplierParts;
import com.watermelon.server.event.parts.domain.Parts;
import com.watermelon.server.event.parts.domain.PartsCategory;
import com.watermelon.server.event.parts.repository.LotteryApplierPartsRepository;
import com.watermelon.server.event.parts.repository.PartsRepository;
import com.watermelon.server.event.lottery.service.LotteryApplierService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.watermelon.server.constants.Constants.TEST_PARTS_ID;
import static com.watermelon.server.auth.service.TestTokenVerifier.TEST_UID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("[단위] 응모자-파츠 서비스")
class LotteryApplierPartsServiceImplTest {

    @Mock
    private LotteryApplierPartsRepository lotteryApplierPartsRepository;

    @Mock
    private PartsRepository partsRepository;

    @Mock
    private LotteryApplierService lotteryApplierService;

    @InjectMocks
    private LotteryApplierPartsServiceImpl lotteryApplierPartsService;

    @Test
    @DisplayName("첫 번째 파츠 추가 - 성공")
    void addPartsAndGetFirstPartsCase() {

        final PartsCategory CATEGORY2 = PartsCategory.COLOR;
        final PartsCategory CATEGORY3 = PartsCategory.WHEEL;

        //given (응모자가 가지고 있는 기존 파츠 중 신규 파츠가 카테고리의 첫 파츠일 때)
        final LotteryApplier lotteryApplier = LotteryApplier.builder()
                .id(anyLong())
                .build();

        final Parts existParts2 = Parts.builder()
                .category(CATEGORY2)
                .build();

        final Parts newParts = Parts.builder()
                .category(CATEGORY3)
                .build();

        final List<LotteryApplierParts> lotteryApplierPartsList = List.of(
                LotteryApplierParts.createApplierParts(true, lotteryApplier, existParts2)
        );

        Mockito.when(lotteryApplierPartsRepository.findLotteryApplierPartsByLotteryApplierId(lotteryApplier.getId()))
                .thenReturn(lotteryApplierPartsList);

        ArgumentCaptor<LotteryApplierParts> captor = ArgumentCaptor.forClass(LotteryApplierParts.class);

        //when (해당 메소드를 호출하면)
        lotteryApplierPartsService.addPartsAndGet(lotteryApplier, newParts);

        //then (장착된 파츠를 저장한다.)
        verify(lotteryApplierPartsRepository).save(captor.capture());
        LotteryApplierParts capturedArgument = captor.getValue();
        assertThat(capturedArgument.isEquipped()).isTrue();

    }

    @Test
    @DisplayName("첫 번째 이후 파츠 추가 - 성공")
    void addPartsAndGetNotFirstPartsCase() {

        //given

        //when (응모자가 가지고 있는 기존 파츠 중 신규 파츠가 카테고리의 첫 파츠가 아닐 때)
        final LotteryApplier lotteryApplier = LotteryApplier.builder().id(anyLong()).build();
        final Parts existParts = Parts.builder().category(PartsCategory.COLOR).build();
        final Parts newParts = Parts.builder().category(PartsCategory.COLOR).build();

        final List<LotteryApplierParts> lotteryApplierPartsList = List.of(
                LotteryApplierParts.createApplierParts(true, lotteryApplier, existParts)
        );

        Mockito.when(lotteryApplierPartsRepository.findLotteryApplierPartsByLotteryApplierId(lotteryApplier.getId()))
                .thenReturn(lotteryApplierPartsList);

        ArgumentCaptor<LotteryApplierParts> captor = ArgumentCaptor.forClass(LotteryApplierParts.class);

        //when (해당 메소드를 호출하면)
        lotteryApplierPartsService.addPartsAndGet(lotteryApplier, newParts);

        //then (장착되지 않은 파츠를 저장한다.)
        verify(lotteryApplierPartsRepository).save(captor.capture());
        LotteryApplierParts capturedArgument = captor.getValue();
        assertThat(capturedArgument.isEquipped()).isFalse();
    }

    @Test
    @DisplayName("파츠 응모권 부여 - 성공")
    void addPartsAndGetHasAllPartsCase() {

        //given
        LotteryApplier lotteryApplier = Mockito.mock(LotteryApplier.class);
        Parts parts = Mockito.mock(Parts.class);
        Mockito.when(partsRepository.countCategoryDistinct()).thenReturn(4L);
        Mockito.when(lotteryApplierPartsRepository.countDistinctPartsCategoryByLotteryApplier(lotteryApplier))
                .thenReturn(4L);

        //when
        lotteryApplierPartsService.addPartsAndGet(lotteryApplier, parts);

        //then
        verify(lotteryApplierService).applyPartsLotteryApplier(lotteryApplier);

    }

    @Test
    @DisplayName("파츠 응모권 부여 - 실패")
    void addPartsAndGetHasNotAllPartsCase() {

        //given
        LotteryApplier lotteryApplier = Mockito.mock(LotteryApplier.class);
        Parts parts = Mockito.mock(Parts.class);
        Mockito.when(partsRepository.countCategoryDistinct()).thenReturn(4L);
        Mockito.when(lotteryApplierPartsRepository.countDistinctPartsCategoryByLotteryApplier(lotteryApplier))
                .thenReturn(3L);

        //when
        lotteryApplierPartsService.addPartsAndGet(lotteryApplier, parts);

        //then
        verify(lotteryApplierService, never()).applyPartsLotteryApplier(lotteryApplier);

    }

    @Test
    @DisplayName("파츠 중복 저장 방지 - 성공")
    void partsAlreadyExist(){

        //given
        LotteryApplier lotteryApplier = Mockito.mock(LotteryApplier.class);
        when(lotteryApplier.getUid()).thenReturn(TEST_UID);

        Parts parts = Mockito.mock(Parts.class);
        when(parts.getId()).thenReturn(TEST_PARTS_ID);

        Mockito.when(
                lotteryApplierPartsRepository.findLotteryApplierPartsByLotteryApplierUidAndPartsId(
                        TEST_UID, TEST_PARTS_ID
                )
        ).thenReturn(Optional.of(Mockito.mock(LotteryApplierParts.class)));

        //when
        lotteryApplierPartsService.addPartsAndGet(lotteryApplier, parts);

        //then
        ArgumentCaptor<LotteryApplierParts> captor = ArgumentCaptor.forClass(LotteryApplierParts.class);
        verify(lotteryApplierPartsRepository, never()).save(captor.capture());

    }


}