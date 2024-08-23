package com.watermelon.server.token;

import com.watermelon.server.OrderApplication;
import com.watermelon.server.order.exception.ApplyTicketWrongException;
import io.jsonwebtoken.Jwts;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = OrderApplication.class)
@DisplayName("[통합] 티켓 프로바이더")
class ApplyTokenProviderTest {

    @Autowired
    ApplyTokenProvider applyTokenProvider;

    @Test
    @DisplayName("JWT APPLY TOKEN 토큰 발급 - 성공")
    void createApplyToken() throws ApplyTicketWrongException {
        String testEventId = "testEventId";
        JwtPayload payload = JwtPayload.builder()
                .eventId(testEventId)
                .build();
        String accessToken = applyTokenProvider.createTokenByOrderEventId(payload);
        Assertions.assertThat(accessToken).isNotNull();

        JwtPayload payLoad1 = applyTokenProvider.verifyToken(accessToken,testEventId);
        Assertions.assertThat(payLoad1.getEventId()).isEqualTo(testEventId);

    }
    @Test
    @DisplayName("JWT APPLY TOKEN 토큰 발급 - 실패 (Wrong secret key 로 발급)")
    void wrongSecretKey(){
        String testEventId = "testQuizId";
        JwtPayload payload = JwtPayload.builder()
                .eventId(testEventId)
                .build();
        String accessToken = Jwts.builder()
                .claim("eventId", payload.getEventId())
                .issuer("test")
                .signWith(Jwts.SIG.HS256.key().build())
                .compact();
        Assertions.assertThatThrownBy(()->applyTokenProvider.verifyToken(accessToken,testEventId))
                .isInstanceOf(ApplyTicketWrongException.class);

    }



}