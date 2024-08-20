package com.watermelon.server.auth.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public interface LoginCheckInterceptor extends HandlerInterceptor {

    String HEADER_AUTHORIZATION = "Authorization";
    String HEADER_UID = "uid";

    @Override
    boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler);

}
