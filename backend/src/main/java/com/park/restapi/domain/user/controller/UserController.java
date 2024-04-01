package com.park.restapi.domain.user.controller;

import com.park.restapi.domain.user.dto.request.LoginInfoRequestDTO;
import com.park.restapi.domain.user.dto.request.SignUpRequstDTO;
import com.park.restapi.domain.user.dto.response.UserInfoResponseDTO;
import com.park.restapi.domain.user.service.impl.UserServiceImpl;
import com.park.restapi.util.response.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class UserController {

    private final UserServiceImpl userService;

    // 회원가입
    @PostMapping("signup")
    public ResponseEntity<ApiResponse<?>> signUp(@RequestBody SignUpRequstDTO dto) throws IOException, InterruptedException {
        userService.signUp(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.createSuccessNoContent("회원가입 완료"));
    }

    // 이메일 중복확인
    @GetMapping("email-check")
    public ResponseEntity<ApiResponse<?>> checkEmail(@RequestParam(name = "email") String email) throws IOException {
        boolean result = userService.existEmailCheck(email);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.createSuccess(result, "true = 사용불가, false = 사용가능"));
    }

    // 로그인
    @PostMapping("login")
    public ResponseEntity<ApiResponse<?>> login(@RequestBody LoginInfoRequestDTO dto, HttpServletResponse response){
        userService.login(dto, response);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.createSuccessNoContent("로그인 성공"));
    }

    // 소셜로그인
    @PostMapping("social-login")
    public ResponseEntity<ApiResponse<?>> socialLogin(HttpServletResponse response){
        userService.socialLogin(response);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.createSuccessNoContent("소셜 로그인 성공"));
    }

    // 토큰 조회
    @GetMapping("tokens")
    public ResponseEntity<ApiResponse<?>> getToken(){
        int token = userService.getToken();

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.createSuccess(token, "토큰 개수"));
    }

    // 로그아웃
    @GetMapping("logout")
    public ResponseEntity<ApiResponse<?>> logout(HttpServletResponse response){
        userService.logout(response);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.createSuccessNoContent("로그아웃 성공"));
    }

    // 유저 정보 조회
    @GetMapping("users")
    public ResponseEntity<ApiResponse<?>> getUserInfo(){
        UserInfoResponseDTO userInfo = userService.getUserInfo();

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.createSuccess(userInfo, "유저 정보"));
    }

}
