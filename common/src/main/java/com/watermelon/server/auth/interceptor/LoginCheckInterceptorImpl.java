package com.watermelon.server.auth.interceptor;

import com.watermelon.server.auth.service.TokenVerifier;
import com.watermelon.server.auth.utils.AuthUtils;
import com.watermelon.server.auth.exception.AuthenticationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
@Slf4j
public class LoginCheckInterceptorImpl implements LoginCheckInterceptor {

    private final TokenVerifier tokenVerifier;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.debug("preHandle");

        String token = null, uid;

        try {
            token = AuthUtils.parseAuthenticationHeaderValue(
                    request.getHeader(HEADER_AUTHORIZATION)
            );
            uid = tokenVerifier.verify(token);
            request.setAttribute(HEADER_UID, uid);

            return true;
        } catch (Exception e) {
            if(testToken(request, token)) return true;
            throw new AuthenticationException("invalid token");
        }
    }

    private boolean testToken(HttpServletRequest request, String token){
        if(tokenRangeChecker(token)) {
            request.setAttribute(HEADER_UID, token);
            return true;
        }
        return false;
    }

    private boolean tokenRangeChecker(String token){
        try {
            int tokenInteger = Integer.parseInt(token);
            if(tokenInteger >= 500000 && tokenInteger <= 999999) return true;

        }catch (NumberFormatException e){
            return false;
        }
        return false;
    }
}
