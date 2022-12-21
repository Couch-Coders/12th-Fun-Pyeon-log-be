package com.example.demo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    
    // 인증 예외
    BAD_REQUEST_PARAM(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    NOT_FOUND_TOKEN(HttpStatus.NOT_FOUND, "엑세스 토큰이 없습니다."),
    AUTHENTICATION_FAILURE(HttpStatus.UNAUTHORIZED, "잘못된 엑세스 토큰입니다."),

    // 유저 예외
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "해당 유저가 존재하지 않습니다."),
    NOT_CORRECT_USER(HttpStatus.FORBIDDEN, "올바른 유저가 아닙니다."),

    // 리뷰 예외
    NOT_FOUND_REVIEW(HttpStatus.NOT_FOUND, "해당 리뷰가 존재하지 않습니다."),

    // 편의점 예외
    NOT_FOUND_STORE(HttpStatus.NOT_FOUND, "해당 편의점이 존재하지 않습니다.");

    private HttpStatus status;
    private String detail;
}
