package com.example.demo.web.exception;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {
    public final BaseResponseCode baseResponseCode;
    public final String message;

    public BaseException(BaseResponseCode baseResponseCode) {
        this.baseResponseCode = baseResponseCode;
        this.message = baseResponseCode.getMessage();
    }

    public BaseException(BaseResponseCode baseResponseCode, String message){
        this.baseResponseCode = baseResponseCode;
        this.message = message;
    }

}