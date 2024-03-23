package com.park.restapi.domain.user.service;

import com.park.restapi.domain.user.dto.request.LoginInfoRequestDTO;
import com.park.restapi.domain.user.dto.request.SignUpRequstDTO;
import com.park.restapi.domain.user.dto.response.LoginInfoResponseDTO;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface UserService {
    // 회원가입
    void signUp(SignUpRequstDTO signUpRequstDTO) throws IOException;
    // 이메일 중복 확인
    boolean existEmailCheck(String email);
    // 로그인
    LoginInfoResponseDTO login(LoginInfoRequestDTO dto, HttpServletResponse response);
    // 토큰 조회
    int getToken();
    // 로그아웃
    void logout(HttpServletResponse response);
    // 소셜로그인
    LoginInfoResponseDTO socialLogin(HttpServletResponse response);

}
