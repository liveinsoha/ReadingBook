package com.example.demo.web.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class BaseException extends RuntimeException {
    private final BaseResponseCode baseResponseCode;
    private final String message;

    public BaseException(BaseResponseCode baseResponseCode) {
        this.baseResponseCode = baseResponseCode;
        this.message = baseResponseCode.getMessage();
    }
}