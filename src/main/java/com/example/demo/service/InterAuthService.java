package com.example.demo.service;

import com.example.demo.dto.FirebaseTokenDTO;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Profile("inter")
public class InterAuthService extends AbstractAuthService {
    public InterAuthService(FirebaseAuth firebaseAuth, UserService userService) {
        super(firebaseAuth, userService);
    }

    @Override
    public FirebaseTokenDTO verifyIdToken(String bearerToken) {
        return new FirebaseTokenDTO("uid-1", "name-1", "admin@gmail.com", "picture-sample");
    }

    @Override
    public void revokeRefreshTokens(String uid) throws FirebaseAuthException {
        log.info("revoke token : {}", uid);
    }
}
