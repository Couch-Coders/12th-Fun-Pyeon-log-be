package com.example.demo.controller;

import com.example.demo.exception.CustomException;
import com.example.demo.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<Object> handleCustomException(CustomException e){
        return ErrorResponse.toResponseEntity(e);
    }

}
