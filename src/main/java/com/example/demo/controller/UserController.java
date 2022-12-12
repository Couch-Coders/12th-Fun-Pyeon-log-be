package com.example.demo.controller;

import com.example.demo.dto.FirebaseTokenDTO;
import com.example.demo.entity.User;
import com.example.demo.service.AbstractAuthService;
import com.example.demo.service.UserService;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    AbstractAuthService authService;

    @GetMapping("/me")
    public ResponseEntity<String> login(@RequestHeader("Authorization") String token) throws FirebaseAuthException {
        FirebaseTokenDTO tokenDTO = authService.verifyIdToken(token);
        User user = authService.loginOrEntry(tokenDTO);
        ResponseCookie responseCookie = createCookie("token", token);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString()).body(user.getEmail());
    }

    @DeleteMapping("/me")
    public ResponseEntity<String> logout(HttpServletRequest request) throws FirebaseAuthException {
        String token = findCookie(request, "token");
        FirebaseTokenDTO tokenDTO = authService.verifyIdToken(token);
        authService.revokeRefreshTokens(tokenDTO.getUid());
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, removeCookie("token").toString()).build();
    }

    @DeleteMapping("")
    public ResponseEntity<String> deleteUser(HttpServletRequest request) throws FirebaseAuthException {
        String token = findCookie(request, "token");
        FirebaseTokenDTO tokenDTO = authService.verifyIdToken(token);
        userService.deleteUser(tokenDTO.getEmail());
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, removeCookie("token").toString()).build();
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
        Cookie[] cookies = request.getCookies();
        for (Cookie c : cookies) {
            if (c.getName().equals(name))
                return c.getName();
        }
        return null;
    }
}
