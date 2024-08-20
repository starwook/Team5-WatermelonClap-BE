package com.watermelon.server.auth.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("dev")
@Component
public class TokenBeUidLoginCheckInterceptor implements LoginCheckInterceptor{
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        request.setAttribute(HEADER_UID, request.getHeader(HEADER_AUTHORIZATION));

        return true;
    }
}