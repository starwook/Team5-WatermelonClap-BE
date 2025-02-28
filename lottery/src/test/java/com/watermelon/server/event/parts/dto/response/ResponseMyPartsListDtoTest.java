package com.watermelon.server.event.parts.dto.response;

import com.watermelon.server.event.lottery.domain.LotteryApplier;
import com.watermelon.server.event.parts.domain.LotteryApplierParts;
import com.watermelon.server.event.parts.domain.Parts;
import com.watermelon.server.event.parts.domain.PartsCategory;
import com.watermelon.server.event.parts.dto.response.ResponseMyPartsListDto;
import com.watermelon.server.event.parts.dto.response.ResponsePartsListDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("[단위] 파츠 리스트 Dto")
class ResponseMyPartsListDtoTest {

    @Test
    @DisplayName("카테고리 별로 분류 - 성공")
    void createDtoListByCategory() {

        //given
        Parts parts1 = Parts.builder()
                .category(PartsCategory.COLOR)
                .build();

        Parts parts2 = Parts.builder()
                .category(PartsCategory.WHEEL)
                .build();

        LotteryApplier lotteryApplier = new LotteryApplier();

        LotteryApplierParts lotteryApplierParts1 = LotteryApplierParts.createApplierParts(true, lotteryApplier, parts1);
        LotteryApplierParts lotteryApplierParts2 = LotteryApplierParts.createApplierParts(true, lotteryApplier, parts2);

        List<ResponseMyPartsListDto> expected = List.of(
                ResponseMyPartsListDto.builder()
                        .category(PartsCategory.COLOR.toString())
                        .parts(List.of(ResponsePartsListDto.from(parts1, true)))
                        .build(),
                ResponseMyPartsListDto.builder()
                        .category(PartsCategory.REAR.toString())
                        .parts(List.of())
                        .build(),
                ResponseMyPartsListDto.builder()
                        .category(PartsCategory.DRIVE_MODE.toString())
                        .parts(List.of())
                        .build(),
                ResponseMyPartsListDto.builder()
                        .category(PartsCategory.WHEEL.toString())
                        .parts(List.of(ResponsePartsListDto.from(parts2, true)))
                        .build()
        );

        //when
        List<ResponseMyPartsListDto> actual = ResponseMyPartsListDto.createDtoListByCategory(
                List.of(lotteryApplierParts1, lotteryApplierParts2)
        );

        //then
        assertThat(actual).isEqualTo(expected);

    }
}