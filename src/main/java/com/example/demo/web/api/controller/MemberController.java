package com.example.demo.web.api.controller;

import com.example.demo.utils.MessageUtils;
import com.example.demo.web.dto.BaseResponse;
import com.example.demo.web.dto.request.MemberRegisterRequest;
import com.example.demo.web.dto.response.SignUpSuccessResponse;
import com.example.demo.web.exception.BaseExceptionResponse;
import com.example.demo.web.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.demo.web.exception.BaseResponseCode.*;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = MessageUtils.SUCCESS),
            @ApiResponse(responseCode = "400", description = MessageUtils.BAD_REQUEST,
                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class)))
    })
    @Operation(summary = "일반 회원가입", description = "account, password 기반 일반 회원가입입니다. <br>리턴 데이터는 회원번호입니다")
    @PostMapping("/register")
    public BaseResponse<SignUpSuccessResponse> register(@ModelAttribute MemberRegisterRequest request){

        return new BaseResponse<>(OK.getHttpStatus(), OK.getMessage(), memberService.register(request));

    }
}