package com.example.demo.controller;

import com.example.demo.consts.AuthConsts;
import com.example.demo.dto.FirebaseTokenDTO;
import com.example.demo.entity.User;
import com.example.demo.service.AbstractAuthService;
import com.example.demo.service.UserService;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    AbstractAuthService authService;

    @GetMapping("/me")
    public ResponseEntity<Map<String, String>> login(@RequestHeader("Authorization") String token) {
        log.info("=========파이어베이스 토큰 유효성 검사 전=========");
        log.info(token);
        log.info("=========파이어베이스 토큰 유효성 검사 전=========");
        FirebaseTokenDTO tokenDTO = authService.verifyIdToken(token);
        User user = authService.loginOrEntry(tokenDTO);
        ResponseCookie responseCookie = createCookie(AuthConsts.accessTokenKey, token);

        log.info("=========파이어베이스 토큰 유효성 검사 후=========");
        log.info(responseCookie.toString());
        log.info("=========파이어베이스 토큰 유효성 검사 후=========");
        Map<String, String> respMap = new HashMap<>();
        respMap.put("email", tokenDTO.getEmail());
        respMap.put("userImageUrl", tokenDTO.getPictureUrl());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(respMap);
    }


    @DeleteMapping("/me")
    public ResponseEntity<String> logout(HttpServletRequest request) throws FirebaseAuthException {
        String token = findCookie(request, AuthConsts.accessTokenKey);
        log.info("=========파이어베이스 토큰 유효성 검사 전=========");
        log.info(token);
        log.info("=========파이어베이스 토큰 유효성 검사 전=========");
        FirebaseTokenDTO tokenDTO = authService.verifyIdToken(token);

        log.info("=========파이어베이스 토큰 유효성 검사 후=========");
        log.info(findCookie(request, AuthConsts.accessTokenKey));
        log.info("=========파이어베이스 토큰 유효성 검사 후=========");
        authService.revokeRefreshTokens(tokenDTO.getUid());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, removeCookie(AuthConsts.accessTokenKey).toString())
                .build();
    }

    @DeleteMapping("")
    public ResponseEntity<String> deleteUser(HttpServletRequest request) throws FirebaseAuthException {
        String token = findCookie(request, AuthConsts.accessTokenKey);
        log.info("=========파이어베이스 토큰 유효성 검사 전=========");
        log.info(token);
        log.info("=========파이어베이스 토큰 유효성 검사 전=========");
        FirebaseTokenDTO tokenDTO = authService.verifyIdToken(token);
        log.info("=========파이어베이스 토큰 유효성 검사 후=========");
        log.info(findCookie(request, AuthConsts.accessTokenKey));
        log.info("=========파이어베이스 토큰 유효성 검사 후=========");
        userService.deleteUser(tokenDTO.getEmail());
        authService.revokeRefreshTokens(tokenDTO.getUid());
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

    public String findCookie(HttpServletRequest request, String name) {
        log.info("findCookie started in UserController");
        Cookie[] cookies = request.getCookies();
        if (cookies == null)
            log.info("cookies is null in FirebaseTokenFilter");
        for (Cookie c : cookies) {
            if (c.getName().equals(name))
                return c.getName();
        }
        return null;
    }
}
