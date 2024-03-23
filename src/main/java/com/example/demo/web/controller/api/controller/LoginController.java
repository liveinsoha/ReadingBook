package com.example.demo.web.controller.api.controller;

import com.example.demo.utils.MessageUtils;
import com.example.demo.web.domain.entity.Member;
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
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;

import static com.example.demo.web.exception.BaseResponseCode.*;

@RestController
@RequiredArgsConstructor
public class LoginController {
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

    @PostMapping("/register/validate/email")
    public ResponseEntity<Object> validateEmail(@RequestParam String email){
        memberService.validatePresentEmail(email);

        BaseResponse response = new BaseResponse(HttpStatus.OK, "사용 가능한 이메일입니다.", true);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PostMapping("/account/find-id")
    public ResponseEntity<Object> findId(@RequestBody HashMap<String, String> request){
        String name = request.get("name");
        String phoneNo = request.get("phoneNo");
        List<String> maskedEmail = memberService.findEmail(name, phoneNo);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(maskedEmail);
    }

    @PostMapping("/account/find-password")
    public ResponseEntity<Object> findPassword(@RequestParam String email, @RequestParam String phoneNo){
        memberService.changePasswordAndSendEmail(email, phoneNo);

        BaseResponse response = new BaseResponse(
                HttpStatus.OK, "비밀번호가 변경되었습니다.", true
        );
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @DeleteMapping("/account/leave")
    public ResponseEntity<Object> leave(Principal principal, String password, HttpServletRequest request){
        Member member = memberService.getMember(principal);

        memberService.leave(member, password);

        /* --- 세션 로그인을 구현한 상태. 로그인 -> 로그아웃으로 변경 --- */
        HttpSession session = request.getSession();
        session.invalidate();

        BaseResponse response = new BaseResponse(
                HttpStatus.OK, "회원탈퇴 처리되었습니다.", true
        );
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}