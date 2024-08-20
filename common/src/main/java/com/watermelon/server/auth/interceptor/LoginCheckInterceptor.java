package com.watermelon.server.auth.interceptor;

import com.watermelon.server.auth.service.TokenVerifier;
import com.watermelon.server.auth.utils.AuthUtils;
import com.watermelon.server.auth.exception.InvalidTokenException;
import com.watermelon.server.auth.exception.AuthenticationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    private final TokenVerifier tokenVerifier;
    private final String HEADER_AUTHORIZATION = "Authorization";
    private final String HEADER_UID = "uid";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.debug("preHandle");

        try {
            String token = AuthUtils.parseAuthenticationHeaderValue(
                    request.getHeader(HEADER_AUTHORIZATION)
            );
            String uid = tokenVerifier.verify(token);
            request.setAttribute(HEADER_UID, uid);

            return true;
        } catch (Exception e) {
            throw new AuthenticationException("invalid token");
        }
    }
}
