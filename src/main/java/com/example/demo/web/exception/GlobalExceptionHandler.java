package com.example.demo.web.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {BaseException.class})
    protected ResponseEntity<BaseExceptionResponse> handleBaseException(BaseException e) {
        return ResponseEntity.status(e.baseResponseCode.getHttpStatus())
                .body(new BaseExceptionResponse(e.baseResponseCode.getHttpStatus(), e.baseResponseCode.getMessage()));
    }
}
