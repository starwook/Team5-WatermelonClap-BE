package com.watermelon.server;

import com.epages.restdocs.apispec.ResourceSnippet;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.watermelon.server.admin.controller.LotteryEventService;
import com.watermelon.server.admin.dto.response.ResponseAdminLotteryWinnerDto;
import com.watermelon.server.admin.dto.response.ResponseAdminPartsWinnerDto;
import com.watermelon.server.admin.dto.response.ResponseLotteryApplierDto;
import com.watermelon.server.event.lottery.dto.response.ResponseLotteryRankDto;
import com.watermelon.server.event.lottery.dto.response.ResponseLotteryWinnerDto;
import com.watermelon.server.event.lottery.dto.response.ResponseLotteryWinnerInfoDto;
import com.watermelon.server.event.lottery.dto.response.ResponseRewardInfoDto;
import com.watermelon.server.event.link.dto.MyLinkDto;
import com.watermelon.server.event.link.service.LinkService;
import com.watermelon.server.event.parts.dto.response.ResponseMyPartsListDto;
import com.watermelon.server.event.parts.dto.response.ResponsePartsDrawDto;
import com.watermelon.server.event.parts.dto.response.ResponseRemainChanceDto;
import com.watermelon.server.event.parts.exception.PartsDrawLimitExceededException;
import com.watermelon.server.event.parts.service.PartsService;
import com.watermelon.server.event.lottery.service.LotteryRewardService;
import com.watermelon.server.event.lottery.service.LotteryService;
import com.watermelon.server.event.lottery.service.LotteryWinnerService;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;

import java.util.List;

import static com.epages.restdocs.apispec.ResourceDocumentation.*;
import static com.watermelon.server.Constants.*;
import static com.watermelon.server.auth.service.TestTokenVerifier.TEST_UID;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;

@AutoConfigureRestDocs
@MockBean(JpaMetamodelMappingContext.class)
@Import({MockLoginInterceptorConfig.class, MockAdminAuthorizationInterceptorConfig.class})
public class ControllerTest extends APITest{

    @MockBean
    protected LotteryService lotteryService;

    @MockBean
    protected LotteryWinnerService lotteryWinnerService;

    @MockBean
    protected LotteryRewardService lotteryRewardService;

    @MockBean
    private PartsService partsService;

    @MockBean
    private LinkService linkService;

    @MockBean
    protected LotteryEventService lotteryEventService;

    protected final String TAG_LOTTERY = "추첨 이벤트";
    protected final String TAG_PARTS = "파츠 이벤트";
    protected final String TAG_ORDER = "선착순 이벤트";
    protected final String TAG_LINK = "링크";
    protected final String TAG_EXPECTATION = "기대평";

    protected ResourceSnippet resourceSnippet(String description) {
        return resource(
                ResourceSnippetParameters.builder()
                        .description(description)
                        .build()
        );
    }

    protected ResourceSnippet resourceSnippetAuthed(String description){

        return resource(
                ResourceSnippetParameters.builder()
                        .description(description)
                        .requestHeaders(
                                headerWithName("Authorization").description("Bearer token for authentication"))
                        .build()
        );

    }

    protected void givenLotteryApplierList() {
        Pageable pageable = PageRequest.of(TEST_PAGE_NUMBER, TEST_PAGE_SIZE);

        Mockito.when(lotteryService.getApplierInfoPage(pageable))
                .thenReturn(new PageImpl<>(
                        List.of(
                                ResponseLotteryApplierDto.createTestDto(),
                                ResponseLotteryApplierDto.createTestDto()
                        ), pageable, TEST_PAGE_SIZE));
    }

    protected void givenPartsWinnerList(){
        Mockito.when(partsService.getAdminPartsWinners())
                .thenReturn(List.of(
                        ResponseAdminPartsWinnerDto.createTestDto(),
                        ResponseAdminPartsWinnerDto.createTestDto()
                ));
    }


    protected void givenLotteryRewardInfo(){
        Mockito.when(lotteryRewardService.getRewardInfo(TEST_RANK)).thenReturn(
                new ResponseRewardInfoDto(TEST_IMGSRC, TEST_NAME)
        );
    }

    protected void givenLotteryWinners() {
        Mockito.when(lotteryWinnerService.getLotteryWinners())
                .thenReturn(List.of(
                        ResponseLotteryWinnerDto.from("email2@email.com", 1)
                ));
    }

    @Override
    protected void givenLotteryWinnersForAdmin() {
        Mockito.when(lotteryWinnerService.getAdminLotteryWinners())
                .thenReturn(List.of(
                                ResponseAdminLotteryWinnerDto.createTestDto(),
                                ResponseAdminLotteryWinnerDto.createTestDto()
                        )
                );
    }


    protected void givenLotteryWinnerInfo() {
        Mockito.when(lotteryWinnerService.getLotteryWinnerInfo(TEST_UID))
                .thenReturn(ResponseLotteryWinnerInfoDto.builder()
                        .name(TEST_NAME)
                        .address(TEST_ADDRESS)
                        .phoneNumber(TEST_PHONE_NUMBER)
                        .build());
    }

    protected void givenLotteryApplierNotExist() {
        Mockito.when(lotteryService.getLotteryRank(TEST_UID)).thenReturn(
                ResponseLotteryRankDto.createNotApplied()
        );
    }

    @Override
    protected void givenLotteryApplierApplied() {

    }

    protected void givenLotteryWinner(){
        Mockito.when(lotteryService.getLotteryRank(TEST_UID)).thenReturn(
                ResponseLotteryRankDto.createAppliedTest()
        );
    }

    protected void givenPartsListForUri(){
        Mockito.when(partsService.getPartsList(TEST_URI)).thenReturn(
                ResponseMyPartsListDto.createTestDtoList()
        );
    }

    @Override
    protected void givenPartsNotEquipped() {

    }

    protected void givenLotteryApplierWhoHasNoRemainChance(){
        Mockito.when(partsService.drawParts(TEST_UID)).thenThrow(new PartsDrawLimitExceededException());
    }

    protected void givenLotteryApplierWhoDrawsPartsFirst(){
        Mockito.when(partsService.drawParts(TEST_UID)).thenReturn(
                ResponsePartsDrawDto.createResponsePartsDrawDtoTest()
        );
    }

    protected void givenLotteryApplierWhoHasRemainChance() {
        Mockito.when(partsService.getRemainChance(TEST_UID)).thenReturn(
                ResponseRemainChanceDto.createTestDto()
        );
    }

    protected void givenMyPartsList(){
        Mockito.when(partsService.getMyParts(TEST_UID)).thenReturn(
                ResponseMyPartsListDto.createTestDtoList()
        );
    }

    protected void givenLink(){
        Mockito.when(linkService.getMyLink(TEST_UID))
                .thenReturn(MyLinkDto.createTestDto());
    }

    protected void givenOriginUri(){
        Mockito.when(linkService.getUrl(TEST_SHORTED_URI)).thenReturn(TEST_URI);
    }

}
