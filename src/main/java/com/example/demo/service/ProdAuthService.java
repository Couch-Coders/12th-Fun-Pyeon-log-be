package com.example.demo.service;

import com.example.demo.dto.FirebaseTokenDTO;
import com.example.demo.entity.User;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@Profile("prod")
public class ProdAuthService extends AbstractAuthService {
    @Override
    public FirebaseTokenDTO verifyIdToken(String bearerToken) {
        try {
            FirebaseToken token = firebaseAuth.verifyIdToken(bearerToken);
            FirebaseTokenDTO tokenDTO = new FirebaseTokenDTO(token);
            return tokenDTO;
        } catch (Exception e) {
            log.error("access token is not usable : {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "토큰 값이 유효하지 않습니다!");
        }
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
