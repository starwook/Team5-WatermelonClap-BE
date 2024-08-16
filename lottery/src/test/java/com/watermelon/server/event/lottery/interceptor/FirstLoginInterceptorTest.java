package com.watermelon.server.event.lottery.interceptor;

import com.watermelon.server.event.link.service.LinkService;
import com.watermelon.server.event.lottery.service.LotteryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.watermelon.server.auth.service.TestTokenVerifier.TEST_UID;
import static com.watermelon.server.common.constants.HttpConstants.HEADER_LINK_ID;
import static com.watermelon.server.common.constants.HttpConstants.HEADER_UID;
import static com.watermelon.server.constants.Constants.*;
import static com.watermelon.server.constants.Constants.TEST_TOKEN;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FirstLoginInterceptorTest {

    @Mock
    private LotteryService lotteryService;

    @Mock
    private LinkService linkService;

    @InjectMocks
    private FirstLoginInterceptor firstLoginInterceptor;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Test
    @DisplayName("처음 로그인할 경우 회원가입한다.")
    void preHandleFirstLoginCaseRegistrationTest(){

        //given
        Mockito.when(request.getAttribute(HEADER_UID)).thenReturn(TEST_UID);
        Mockito.when(lotteryService.isExist(TEST_UID)).thenReturn(false);

        //when
        firstLoginInterceptor.preHandle(request, response, null);

        //then
        verify(lotteryService).registration(TEST_UID);

    }

    @Test
    @DisplayName("처음 로그인하고, 특정 링크로 부터 접속했을 경우 해당 링크의 조회수를 증가시킨다.")
    void preHandleFirstLoginCaseLinkViewTest(){

        //given
        Mockito.when(request.getAttribute(HEADER_UID)).thenReturn(HEADER_UID);
        Mockito.when(request.getHeader(HEADER_LINK_ID)).thenReturn(TEST_URI);

        //when
        firstLoginInterceptor.preHandle(request, response, null);

        //then
        verify(linkService).addLinkViewCount(TEST_URI);

    }

}