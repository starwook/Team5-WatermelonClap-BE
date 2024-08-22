package com.watermelon.server.event.parts.service;

import com.watermelon.server.event.lottery.domain.LotteryApplier;
import com.watermelon.server.event.parts.domain.LotteryApplierParts;
import com.watermelon.server.event.parts.domain.Parts;
import com.watermelon.server.event.parts.domain.PartsCategory;
import com.watermelon.server.event.parts.dto.response.ResponsePartsDrawDto;
import com.watermelon.server.event.parts.exception.PartsDrawLimitExceededException;
import com.watermelon.server.event.parts.repository.PartsRepository;
import com.watermelon.server.event.lottery.service.LotteryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.watermelon.server.auth.service.TestTokenVerifier.TEST_UID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("[단위] 파츠 서비스")
class PartsServiceImplTest {

    @Mock
    private PartsRepository partsRepository;

    @Mock
    private LotteryService lotteryService;

    @Mock
    private LotteryApplierPartsService lotteryApplierPartsService;

    @InjectMocks
    private PartsServiceImpl partsService;

    @Test
    @DisplayName("파츠 뽑기 - 성공")
    void drawParts() {

        //given
        LotteryApplier applier = LotteryApplier.builder()
                .remainChance(1)
                .build();

        Parts parts = Parts.builder()
                .category(PartsCategory.COLOR)
                .build();

        when(lotteryService.applyAndGet(TEST_UID)).thenReturn(applier);
        when(partsRepository.findAll()).thenReturn(List.of(parts));
        when(lotteryApplierPartsService.addPartsAndGet(eq(applier), eq(parts)))
                .thenReturn(LotteryApplierParts.createApplierParts(true, applier, parts));

        // When
        ResponsePartsDrawDto response = partsService.drawParts(TEST_UID);

        // Then
        assertEquals(parts.getName(), response.getName());
        assertTrue(response.isEquipped());

        verify(lotteryService, times(1)).applyAndGet(TEST_UID);
        verify(lotteryApplierPartsService, times(1)).addPartsAndGet(eq(applier), eq(parts));

    }

    @Test
    @DisplayName("파츠 뽑기 - 실패 (뽑기권 없음)")
    void drawPartsFailureCase(){

        //given
        LotteryApplier applier = LotteryApplier.createHasNoRemainChance(TEST_UID);

        when(lotteryService.applyAndGet(TEST_UID)).thenReturn(applier);

        //when & then
        assertThrows(PartsDrawLimitExceededException.class, () -> partsService.drawParts(TEST_UID));

    }
}