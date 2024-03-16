package com.example.demo.web.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class BaseException extends RuntimeException {
    public final BaseResponseCode baseResponseCode;
    public final String message;

    public BaseException(BaseResponseCode baseResponseCode) {
        this.baseResponseCode = baseResponseCode;
        this.message = baseResponseCode.getMessage();
    }
}