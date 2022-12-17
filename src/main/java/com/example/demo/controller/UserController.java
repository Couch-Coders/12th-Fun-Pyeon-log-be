package com.example.demo.controller;

import com.example.demo.consts.AuthConsts;
import com.example.demo.dto.FirebaseTokenDTO;
import com.example.demo.entity.User;
import com.example.demo.service.auth.AuthService;
import com.example.demo.service.UserService;
import com.google.firebase.auth.FirebaseAuthException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    AuthService authService;

    @GetMapping("/me")
    public ResponseEntity<Map<String, String>> login(@RequestHeader("Authorization") String token) {
        FirebaseTokenDTO tokenDTO = authService.verifyIdToken(token);
        authService.loginOrEntry(tokenDTO);

        Map<String, String> respMap = new HashMap<>();
        respMap.put("email", tokenDTO.getEmail());
        respMap.put("userImageUrl", tokenDTO.getPictureUrl());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, createCookie(AuthConsts.accessTokenKey, token).toString())
                .body(respMap);
    }


    @DeleteMapping("/me")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, removeCookie(AuthConsts.accessTokenKey).toString())
                .build();
    }

    @DeleteMapping("")
    public ResponseEntity<String> deleteUser(@AuthenticationPrincipal User user) throws FirebaseAuthException {
        userService.deleteUser(user.getEmail());
        authService.revokeRefreshTokens(user.getUid());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, removeCookie(AuthConsts.accessTokenKey).toString())
                .build();
    }

    public ResponseCookie createCookie(String key, String value){
        return ResponseCookie.from(key, value)
                .maxAge(1 * 60 * 60 * 24 * 365)
                .sameSite("Lax")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .build();
    }

    public ResponseCookie removeCookie(String key){
        return ResponseCookie.from(key, "")
                .maxAge(0)
                .sameSite("Lax")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .build();
    }
}
