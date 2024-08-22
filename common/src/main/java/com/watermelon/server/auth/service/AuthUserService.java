package com.watermelon.server.auth.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.watermelon.server.auth.exception.AuthenticationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthUserService {

    private final FirebaseAuth firebaseAuth;

    public String getUserEmail(String uid) {

        try {
            UserRecord userRecord = firebaseAuth.getUser(uid);
            return userRecord.getEmail();

        }catch (FirebaseAuthException e) {
            throw new AuthenticationException(e.getMessage());
        }

    }

}
