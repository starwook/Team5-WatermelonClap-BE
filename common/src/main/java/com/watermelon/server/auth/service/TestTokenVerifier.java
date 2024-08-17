package com.watermelon.server.auth.service;

import com.watermelon.server.auth.exception.InvalidTokenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("!deploy")
@RequiredArgsConstructor
@Slf4j
public class TestTokenVerifier implements TokenVerifier{

    public static String TEST_VALID_TOKEN = "test_valid_token";
    public static String TEST_INVALID_TOKEN = "test_invalid_token";
    public static String TEST_UID = "TEST_UID";

    @Override
    public String verify(String token) throws InvalidTokenException {
        if(!token.equals(TEST_VALID_TOKEN)) throw new InvalidTokenException();
        return TEST_UID;
    }

}