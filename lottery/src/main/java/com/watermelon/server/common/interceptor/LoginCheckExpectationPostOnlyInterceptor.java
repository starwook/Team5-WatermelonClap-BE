package com.watermelon.server.common.interceptor;

import com.watermelon.server.auth.interceptor.LoginCheckInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class LoginCheckExpectationPostOnlyInterceptor implements HandlerInterceptor {

    private final LoginCheckInterceptor loginCheckInterceptor;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("POST".equalsIgnoreCase(request.getMethod())) {
            return loginCheckInterceptor.preHandle(request, response, handler);
        }
        return true; // POST가 아닌 경우에는 인터셉터를 통과시킴
    }
}