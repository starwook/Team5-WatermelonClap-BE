package com.watermelon.server.event.lottery.service;

import com.watermelon.server.event.lottery.domain.LotteryApplier;
import com.watermelon.server.event.lottery.dto.response.ResponseLotteryRankDto;
import com.watermelon.server.event.lottery.repository.LotteryApplierRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static com.watermelon.server.constants.Constants.*;
import static com.watermelon.server.auth.service.TestTokenVerifier.TEST_UID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("[단위] 추첨 서비스")
class LotteryServiceImplTest {

    @Mock
    private LotteryApplierRepository lotteryApplierRepository;

    @InjectMocks
    private LotteryServiceImpl lotteryService;

    @Test
    @DisplayName("순위 반환 - 성공")
    void getLotteryRankPresentationCase() {

        //given
        Mockito.when(lotteryApplierRepository.findByUid(TEST_UID)).thenReturn(
                Optional.of(LotteryApplier.builder().lotteryRank(TEST_RANK).isLotteryApplier(true).build())
        );

        //when
        ResponseLotteryRankDto responseLotteryRankDto = lotteryService.getLotteryRank(TEST_UID);

        //then
        assertThat(responseLotteryRankDto.getRank()).isEqualTo(TEST_RANK);
        assertThat(responseLotteryRankDto.isApplied()).isTrue();

    }

    @Test
    @DisplayName("순위 반환 - 실패")
    void getLotteryRankNotFoundCase() {
        //given
        Mockito.when(lotteryApplierRepository.findByUid(TEST_UID))
                .thenThrow(new NoSuchElementException());

        //when & then
        assertThatThrownBy(()->lotteryService.getLotteryRank(TEST_UID)).isInstanceOf(NoSuchElementException.class);

    }

    @Test
    @DisplayName("응모 - 성공")
    void applyAndGet() {

        //given
        Mockito.when(lotteryApplierRepository.findByUid(TEST_UID)).thenReturn(
                Optional.ofNullable(LotteryApplier.builder()
                        .uid(TEST_UID)
                        .isLotteryApplier(false)
                        .build())
        );

        //when
        lotteryService.applyAndGet(TEST_UID);

        ArgumentCaptor<LotteryApplier> captor = ArgumentCaptor.forClass(LotteryApplier.class);

        //then
        verify(lotteryApplierRepository).save(captor.capture());
        assertThat(captor.getValue().isLotteryApplier()).isTrue();

    }

    @Test
    @DisplayName("회원가입 - 성공")
    void registration() {

        //given
        ArgumentCaptor<LotteryApplier> captor = ArgumentCaptor.forClass(LotteryApplier.class);

        //when
        lotteryService.registration(TEST_UID);

        //then
        verify(lotteryApplierRepository, times(1)).save(captor.capture());

        assertThat(captor.getValue().getUid()).isEqualTo(TEST_UID);

    }

}