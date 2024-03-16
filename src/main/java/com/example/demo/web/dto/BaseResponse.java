package com.example.demo.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class BaseResponse<T> {

    @Schema(description = "http 상태코드" , example = "OK")
    private HttpStatus httpStatus;  // 상태 코드 메세지

    @Schema(description = "전달 메시지", example = "요청이 성공적으로 처리되었습니다.")
    private String message; // 에러 설명

    @Schema(description = "전달 데이터")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

//    public BaseResponse()
}