package com.park.restapi.domain.member.service;

import com.park.restapi.domain.member.dto.request.LoginInfoRequestDTO;
import com.park.restapi.domain.member.dto.request.SignUpRequstDTO;
import com.park.restapi.domain.member.dto.response.MemberInfoResponseDTO;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface MemberService {
    // 회원가입
    void signUp(SignUpRequstDTO signUpRequstDTO) throws IOException, InterruptedException;
    // 이메일 중복 확인
    boolean existEmailCheck(String email);
    // 로그인
    void login(LoginInfoRequestDTO dto, HttpServletResponse response);
    // 토큰 조회
    int getToken();
    // 로그아웃
    void logout(HttpServletResponse response);
    // 소셜로그인
    void socialLogin(HttpServletResponse response);
    // 유저 정보 호출
    MemberInfoResponseDTO getUserInfo();
}
