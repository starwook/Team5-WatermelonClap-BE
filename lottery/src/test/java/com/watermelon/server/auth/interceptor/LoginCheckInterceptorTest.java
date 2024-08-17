package com.watermelon.server.auth.interceptor;

import com.watermelon.server.auth.exception.AuthenticationException;
import com.watermelon.server.auth.service.TokenVerifier;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.watermelon.server.constants.Constants.*;
import static com.watermelon.server.auth.service.TestTokenVerifier.TEST_UID;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class LoginCheckInterceptorTest {

    @Mock
    private TokenVerifier tokenVerifier;

    @InjectMocks
    private LoginCheckInterceptor loginCheckInterceptor;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Test
    @DisplayName("tokenVerifier 가 UID 를 정상적으로 반환하면 true 를 반환한다.")
    void preHandleSuccessCase() {

        //given
        mockVerifyForUser();

        //when
        boolean actual = loginCheckInterceptor.preHandle(
                request,
                response, null);

        //then
        Assertions.assertThat(actual).isTrue();
    }

    @Test
    @DisplayName("tokenVerifier 가 예외를 발생시키면 Authentication Exception 을 발생시킨다.")
    void preHandleFailureCase() {

        //given
        mockNotVerifyForUser();

        //when & then
        Assertions.assertThatThrownBy(() -> loginCheckInterceptor.preHandle(
                request,
                response, null)).isInstanceOf(AuthenticationException.class);

    }

    private void mockNotVerifyForUser(){
        Mockito.doThrow(AuthenticationException.class).when(tokenVerifier).verify(TEST_TOKEN);
        Mockito.when(request.getHeader(HEADER_NAME_AUTHORIZATION)).thenReturn(HEADER_VALUE_BEARER+HEADER_VALUE_SPACE+TEST_TOKEN);
    }

    private void mockVerifyForUser(){
        Mockito.when(request.getHeader(HEADER_NAME_AUTHORIZATION)).thenReturn(HEADER_VALUE_BEARER+HEADER_VALUE_SPACE+TEST_TOKEN);
        Mockito.when(tokenVerifier.verify(TEST_TOKEN)).thenReturn(TEST_UID);
    }

}
