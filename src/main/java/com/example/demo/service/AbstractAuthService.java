package com.example.demo.service;

import com.example.demo.dto.FirebaseTokenDTO;
import com.example.demo.entity.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public abstract class AbstractAuthService {

    @Autowired
    FirebaseAuth firebaseAuth;

    @Autowired
    UserService userService;

    public abstract FirebaseTokenDTO verifyIdToken(String bearerToken);

    public abstract void revokeRefreshTokens(String uid) throws FirebaseAuthException;

    public User loginOrEntry(FirebaseTokenDTO tokenDTO) {
        User user = null;
        try {
            user = userService.getUser(tokenDTO.getEmail());
        } catch (Exception e) {
            log.error("User with email {} was not found in the database, creating user", tokenDTO.getEmail());
            user = userService.addUser(tokenDTO.getEmail());
        }

        return user;
    }
}
