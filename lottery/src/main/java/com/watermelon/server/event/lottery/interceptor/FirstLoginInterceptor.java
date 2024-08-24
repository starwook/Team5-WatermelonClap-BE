package com.watermelon.server.event.lottery.interceptor;

import com.watermelon.server.event.lottery.service.LotteryService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;

import static com.watermelon.server.common.constants.HttpConstants.HEADER_LINK_ID;
import static com.watermelon.server.common.constants.HttpConstants.HEADER_UID;

@Component
@RequiredArgsConstructor
@Slf4j
public class FirstLoginInterceptor implements HandlerInterceptor {

    private final LotteryService lotteryService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        String linkId = null;
        Cookie[] cookies = request.getCookies();

        log.info("cookies: {}", Arrays.toString(cookies));

        if(cookies != null) {
            for(Cookie cookie : cookies) {
                String cookieName = cookie.getName();
                if(cookieName.equals(HEADER_LINK_ID)) linkId = cookie.getValue();
            }
        }

        log.info("linkId:{}", linkId);

        boolean success = false;
        for(int i=0; i<300&&!success; i++){
            try {
                lotteryService.firstLogin(
                        request.getAttribute(HEADER_UID).toString(),
                        linkId
                );
                success = true;
            }catch (ObjectOptimisticLockingFailureException e){
                log.info("first login failure:{}", i);
            }
        }

        return true;
    }

}
