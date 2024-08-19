package com.watermelon.server.event.lottery.interceptor;

import com.watermelon.server.event.link.service.LinkService;
import com.watermelon.server.event.lottery.service.LotteryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import static com.watermelon.server.common.constants.HttpConstants.*;

@Component
@RequiredArgsConstructor
public class FirstLoginInterceptor implements HandlerInterceptor {

    private final LotteryService lotteryService;
    private final LinkService linkService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        checkFirstLogin(
                request.getAttribute(HEADER_UID).toString(),
                request.getHeader(HEADER_LINK_ID)
        );

        return true;
    }

    private void checkFirstLogin(String uid, String linkId){
        if(lotteryService.isExist(uid)) return;

        //만약 등록되지 않은 유저라면
        lotteryService.registration(uid);

        if(linkId==null || linkId.isEmpty()) return;

        //링크 아이디가 존재한다면
        linkService.addLinkViewCount(linkId);

    }

}
