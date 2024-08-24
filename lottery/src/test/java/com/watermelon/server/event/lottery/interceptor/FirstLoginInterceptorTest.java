package com.watermelon.server.event.lottery.interceptor;

import com.watermelon.server.event.link.service.LinkService;
import com.watermelon.server.event.lottery.service.LotteryService;
import jakarta.servlet.http.Cookie;
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
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("[단위] 첫 로그인 인터셉터")
class FirstLoginInterceptorTest {

    @Mock
    private LotteryService lotteryService;

    @InjectMocks
    private FirstLoginInterceptor firstLoginInterceptor;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Test
    @DisplayName("첫 로그인 - 성공")
    void preHandleFirstLoginCaseRegistrationTest(){

        //given
        Mockito.when(request.getAttribute(HEADER_UID)).thenReturn(TEST_UID);
        Mockito.when(request.getCookies()).thenReturn(new Cookie[]{new Cookie(HEADER_LINK_ID, TEST_URI)});

        //when
        firstLoginInterceptor.preHandle(request, response, null);

        //then
        verify(lotteryService).firstLogin(TEST_UID, TEST_URI);

    }

//    @Test
//    @DisplayName("링크 조회수 증가 - 성공")
//    void preHandleFirstLoginCaseLinkViewTest(){
//
//        //given
//        Mockito.when(request.getAttribute(HEADER_UID)).thenReturn(HEADER_UID);
//        Mockito.when(request.getCookies()).thenReturn(new Cookie[]{new Cookie(HEADER_LINK_ID, TEST_URI)});
//
//        //when
//        firstLoginInterceptor.preHandle(request, response, null);
//
//        //then
//        verify(linkService).addLinkViewCount(TEST_URI);
//
//    }

}