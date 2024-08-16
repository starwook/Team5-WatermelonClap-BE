package com.watermelon.server.auth.interceptor;

import com.watermelon.server.auth.exception.InvalidTokenException;
import com.watermelon.server.auth.service.TokenVerifier;
import com.watermelon.server.auth.utils.AuthUtils;
import com.watermelon.server.auth.exception.AuthenticationException;
import com.watermelon.server.event.link.service.LinkService;
import com.watermelon.server.event.lottery.service.LotteryService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;

import static com.watermelon.server.common.constants.HttpConstants.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    private final TokenVerifier tokenVerifier;
    private final LotteryService lotteryService;
    private final LinkService linkService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.debug("preHandle");

        String token = AuthUtils.parseAuthenticationHeaderValue(
                request.getHeader(HEADER_AUTHORIZATION)
        );
        try {
            String uid = tokenVerifier.verify(token);
            request.setAttribute(HEADER_UID, uid);

            checkFirstLogin(uid, request);

            return true;
        }catch (InvalidTokenException e) {
            throw new AuthenticationException("invalid token");
        }
    }

    private void checkFirstLogin(String uid, HttpServletRequest request){

        if(lotteryService.isExist(uid)) return;

        //만약 등록되지 않은 유저라면
        lotteryService.registration(uid);

        //쿠키가 없다면
        if(request.getCookies()==null) return;

        String linkId = Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals(HEADER_LINK_ID))
                .map(Cookie::getValue)
                .findFirst().orElse(null);

        if(linkId==null || linkId.isEmpty()) return;

        //링크 아이디가 존재한다면
        linkService.addLinkViewCount(linkId);

    }

}
