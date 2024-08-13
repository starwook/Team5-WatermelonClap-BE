package com.watermelon.server;

import com.watermelon.server.auth.service.TestTokenVerifier;
import com.watermelon.server.auth.service.TokenVerifier;
import com.watermelon.server.event.link.service.LinkService;
import com.watermelon.server.event.lottery.service.LotteryService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import static com.watermelon.server.auth.service.TestTokenVerifier.TEST_UID;

@TestConfiguration
public class MockLoginInterceptorConfig {

    @Bean
    public TokenVerifier mockTokenVerifier() {
        TokenVerifier tokenVerifier = Mockito.mock(TokenVerifier.class);
        Mockito.when(tokenVerifier.verify(TestTokenVerifier.TEST_VALID_TOKEN)).thenReturn(TEST_UID);
        return tokenVerifier;
    }

    @Bean
    public LinkService mockLinkService() {
        return Mockito.mock(LinkService.class);
    }

    @Bean
    public LotteryService mockLotteryService() {
        return Mockito.mock(LotteryService.class);
    }

}
