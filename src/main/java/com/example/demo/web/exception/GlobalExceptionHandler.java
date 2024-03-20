package com.example.demo.web.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {BaseException.class})
    protected ResponseEntity<BaseExceptionResponse> handleBaseException(BaseException e) {
        log.info("예외 발생 : code : {} , message : {}",e.baseResponseCode.getHttpStatus(), e.baseResponseCode.getHttpStatus());

        return ResponseEntity.status(e.baseResponseCode.getHttpStatus())
                .body(new BaseExceptionResponse(e.baseResponseCode.getHttpStatus(), e.baseResponseCode.getMessage()));
    }
}
