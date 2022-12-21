package com.example.demo.service.auth;

import com.example.demo.dto.FirebaseTokenDTO;
import com.example.demo.entity.User;
import com.example.demo.exception.CustomException;
import com.example.demo.service.UserService;
import com.google.firebase.auth.FirebaseAuthException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public abstract class AuthService {
    UserService userService;

    public abstract FirebaseTokenDTO verifyIdToken(String bearerToken);

    public abstract void revokeRefreshTokens(String uid) throws FirebaseAuthException;

    public User loginOrEntry(FirebaseTokenDTO tokenDTO) {
        User user = null;
        try {
            user = userService.getUser(tokenDTO.getEmail());
        } catch (CustomException e) {
            log.error("User with email {} was not found in the database, creating user", tokenDTO.getEmail());
            user = userService.addUser(tokenDTO.getEmail());
        }
        return user;
    }
}
