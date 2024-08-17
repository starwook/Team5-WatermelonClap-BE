package com.watermelon.server.config;

import com.watermelon.server.auth.service.TestTokenVerifier;
import com.watermelon.server.auth.service.TokenVerifier;
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

}
