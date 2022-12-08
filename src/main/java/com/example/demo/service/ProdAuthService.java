package com.example.demo.service;

import com.example.demo.dto.FirebaseTokenDTO;
import com.example.demo.entity.User;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Profile("prod")
public class ProdAuthService extends AbstractAuthService {
    @Override
    public FirebaseTokenDTO verifyIdToken(String bearerToken) throws FirebaseAuthException {
        FirebaseToken token = firebaseAuth.verifyIdToken(bearerToken);
        FirebaseTokenDTO tokenDTO = new FirebaseTokenDTO(token);
        return tokenDTO;
    }

    @Override
    public void revokeRefreshTokens(String uid) throws FirebaseAuthException {
        try {
            firebaseAuth.revokeRefreshTokens(uid);
        } catch (FirebaseAuthException e) {
            log.error("revoke token error : {}", e.getMessage());
            throw new FirebaseAuthException(e);
        }
    }
}
