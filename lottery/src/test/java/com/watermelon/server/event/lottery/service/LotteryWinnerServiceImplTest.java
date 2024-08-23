package com.watermelon.server.event.lottery.service;

import com.watermelon.server.event.lottery.domain.LotteryApplier;
import com.watermelon.server.event.lottery.dto.request.RequestLotteryWinnerInfoDto;
import com.watermelon.server.event.lottery.dto.response.ResponseLotteryWinnerDto;
import com.watermelon.server.event.lottery.dto.response.ResponseLotteryWinnerInfoDto;
import com.watermelon.server.event.lottery.repository.LotteryApplierRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.watermelon.server.constants.Constants.*;
import static com.watermelon.server.auth.service.TestTokenVerifier.TEST_UID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("[단위] 추첨 당첨자 서비스")
class LotteryWinnerServiceImplTest {

    @Mock
    private LotteryApplierRepository lotteryApplierRepository;

    @InjectMocks
    private LotteryWinnerServiceImpl lotteryWinnerServiceImpl;

    @Test
    @DisplayName("당첨자 목록 조회 - 성공")
    void getLotteryWinners() {

        //given
        List<ResponseLotteryWinnerDto> expected = List.of(
                ResponseLotteryWinnerDto.from("email1@email.com", 1)
        );

        Mockito.when(lotteryApplierRepository.findByLotteryRankNot(-1)).thenReturn(
                List.of(
                        LotteryApplier.builder()
                                .lotteryRank(1)
                                .email("email1@email.com")
                                .build()
                )
        );

        //when
        List<ResponseLotteryWinnerDto> actual = lotteryWinnerServiceImpl.getLotteryWinners();

        //then
        assertThat(actual).isEqualTo(expected);

    }

    @Test
    @DisplayName("당첨자 정보 조회 - 성공")
    void getLotteryWinnerInfo() {

        //given
        final LotteryApplier lotteryApplier = LotteryApplier.builder()
                .name(TEST_NAME)
                .address(TEST_ADDRESS)
                .phoneNumber(TEST_PHONE_NUMBER)
                .uid(TEST_UID)
                .build();

        final ResponseLotteryWinnerInfoDto responseLotteryWinnerInfoDto = ResponseLotteryWinnerInfoDto.from(lotteryApplier);

        Mockito.when(lotteryApplierRepository.findByUid(TEST_UID)).thenReturn(Optional.of(lotteryApplier));

        //when
        ResponseLotteryWinnerInfoDto actual = lotteryWinnerServiceImpl.getLotteryWinnerInfo(TEST_UID);

        //then
        assertThat(actual).isEqualTo(responseLotteryWinnerInfoDto);

    }

    @Test
    @DisplayName("당첨자 정보 저장 - 성공")
    void createLotteryWinnerInfoSuccess(){
        //given
        LotteryApplier lotteryApplier = LotteryApplier.builder().uid(TEST_UID).build();

        Mockito.when(lotteryApplierRepository.findByUid(TEST_UID)).thenReturn(
                Optional.of(lotteryApplier));

        RequestLotteryWinnerInfoDto requestLotteryWinnerInfoDto = RequestLotteryWinnerInfoDto.builder()
                .phoneNumber(TEST_PHONE_NUMBER)
                .address(TEST_ADDRESS)
                .name(TEST_NAME)
                .build();

        //when
        lotteryWinnerServiceImpl.createLotteryWinnerInfo(TEST_UID, requestLotteryWinnerInfoDto);

        //then
        assertThat(lotteryApplier.getPhoneNumber()).isEqualTo(TEST_PHONE_NUMBER);
        assertThat(lotteryApplier.getAddress()).isEqualTo(TEST_ADDRESS);
        assertThat(lotteryApplier.getName()).isEqualTo(TEST_NAME);
        verify(lotteryApplierRepository).save(lotteryApplier);

    }

    @Test
    @DisplayName("당첨자 정보 저장 - 실패")
    void createLotteryWinnerInfoFailure() {
        //given
        Mockito.when(lotteryApplierRepository.findByUid(TEST_UID)).thenReturn(Optional.empty());

        //then
        assertThatThrownBy(()->lotteryWinnerServiceImpl.createLotteryWinnerInfo(TEST_UID, RequestLotteryWinnerInfoDto.builder().build()))
                .isInstanceOf(NoSuchElementException.class);
    }


}