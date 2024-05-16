package com.park.restapi.domain.member.controller;

import com.park.restapi.domain.member.dto.request.LoginInfoRequestDTO;
import com.park.restapi.domain.member.dto.request.SignUpRequestDTO;
import com.park.restapi.domain.member.dto.response.MemberInfoResponseDTO;
import com.park.restapi.domain.member.service.MemberService;
import com.park.restapi.domain.member.service.impl.MemberServiceImpl;
import com.park.restapi.util.response.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping("/api")
public class MemberController {

    private final MemberService memberService;

    // 회원가입
    @PostMapping("signup")
    public ResponseEntity<ApiResponse<Void>> signUp(@Valid @RequestBody SignUpRequestDTO dto) throws IOException, InterruptedException {
        memberService.signUp(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.createSuccessNoContent("회원가입이 완료되었습니다."));
    }

    // 이메일 중복확인
    @GetMapping("email-check")
    public ResponseEntity<ApiResponse<?>> checkEmail(@NotBlank(message = "이메일을 입력해주세요.") @Email(message = "이메일 형식이 아닙니다.")
                                                         @RequestParam(name = "email") String email) throws IOException {
        boolean result = memberService.existEmailCheck(email);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.createSuccess(result, "true = 사용불가, false = 사용가능"));
    }

    // 로그인
    @PostMapping("login")
    public ResponseEntity<ApiResponse<?>> login(@Valid @RequestBody LoginInfoRequestDTO dto, HttpServletResponse response) {
        memberService.login(dto, response);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.createSuccessNoContent("로그인 성공"));
    }

    // 소셜로그인
    @PostMapping("social-login")
    public ResponseEntity<ApiResponse<?>> socialLogin(HttpServletResponse response) {
        memberService.socialLogin(response);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.createSuccessNoContent("소셜 로그인 성공"));
    }

    // 토큰 조회
    @GetMapping("tokens")
    public ResponseEntity<ApiResponse<?>> getToken() {
        int token = memberService.getToken();

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.createSuccess(token, "토큰 개수 조회 성공"));
    }

    // 로그아웃
    @GetMapping("logout")
    public ResponseEntity<ApiResponse<?>> logout(HttpServletResponse response) {
        memberService.logout(response);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.createSuccessNoContent("로그아웃 성공"));
    }

    // 유저 정보 조회
    @GetMapping("users")
    public ResponseEntity<ApiResponse<MemberInfoResponseDTO>> getUserInfo() {
        MemberInfoResponseDTO userInfo = memberService.getUserInfo();

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.createSuccess(userInfo, "현재 로그인 유저의 정보"));
    }

}
