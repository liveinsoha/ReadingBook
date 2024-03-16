package com.example.demo.web.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class BaseExceptionResponse {

    @Schema(description = "http 상태코드", example = "ERROR_REASON")
    private HttpStatus httpStatus;  // 상태 코드 메세지

    @Schema(description = "전달 메시지", example = "이러이러한 이유로 안 됨")
    private String message; // 에러 설명

}