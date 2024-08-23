package com.watermelon.server.event.link.service;

import com.watermelon.server.event.lottery.domain.Link;
import com.watermelon.server.event.lottery.domain.LotteryApplier;
import com.watermelon.server.event.link.dto.MyLinkDto;
import com.watermelon.server.event.link.repository.LinkRepository;
import com.watermelon.server.event.lottery.service.LotteryService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static com.watermelon.server.constants.Constants.TEST_SHARE_URL;
import static com.watermelon.server.constants.Constants.TEST_URI;
import static com.watermelon.server.auth.service.TestTokenVerifier.TEST_UID;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("[단위] 링크 서비스")
class LinkServiceImplTest {

    @Mock
    private LotteryService lotteryService;

    @Mock
    private LinkRepository linkRepository;

    @InjectMocks
    private LinkServiceImpl linkService;

    @BeforeEach
    void setUp() {
        // baseUrl 필드에 강제로 값 주입
        ReflectionTestUtils.setField(linkService, "baseUrl", "http://localhost:8080");
    }

    @Test
    @DisplayName("응모자에 대한 링크 반환 - 성공")
    void getShortedLink() {

        //given
        LotteryApplier lotteryApplier = LotteryApplier.builder()
                .link(
                        Link.builder()
                                .uri(TEST_URI)
                                .build()
                )
                .build();

        Mockito.when(lotteryService.findLotteryApplierByUid(TEST_UID)).thenReturn(
                lotteryApplier
        );

        MyLinkDto expected = MyLinkDto.create(TEST_SHARE_URL);

        //when
        MyLinkDto actual = linkService.getShortedLink(TEST_UID);

        //then
        assertEquals(expected, actual);

    }

    @Test
    @DisplayName("링크 조회수 증가 - 성공")
    void addLinkViewCount() {

        //given
        Link link = Link.createLink(Mockito.mock(LotteryApplier.class));
        Mockito.when(linkRepository.findByUri(TEST_URI)).thenReturn(Optional.ofNullable(link));

        //when
        linkService.addLinkViewCount(TEST_URI);

        //then
        Mockito.verify(linkRepository).incrementViewCount(TEST_URI);
    }
}