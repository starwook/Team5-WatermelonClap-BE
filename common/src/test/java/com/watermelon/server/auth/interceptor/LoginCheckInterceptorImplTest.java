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
@DisplayName("[단위] 로그인 검증 인터셉터")
class LoginCheckInterceptorImplTest {

    @Mock
    private TokenVerifier tokenVerifier;

    @InjectMocks
    private LoginCheckInterceptorImpl loginCheckInterceptor;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Test
    @DisplayName("로그인 검증 - 성공")
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
    @DisplayName("로그인 검증 - 실패")
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
