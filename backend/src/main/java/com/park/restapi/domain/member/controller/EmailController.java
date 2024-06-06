package com.park.restapi.domain.member.controller;

import com.park.restapi.domain.member.dto.request.AuthenticationNumberRequestDTO;
import com.park.restapi.domain.member.dto.request.EmailRequestDTO;
import com.park.restapi.domain.member.service.EmailService;
import com.park.restapi.util.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    // 인증번호 전송
    @PostMapping("/authentication/send")
    public ResponseEntity<ApiResponse<Void>> createAuthenticationNumber(@Valid @RequestBody EmailRequestDTO emailRequestDTO) throws Exception {
        emailService.sendSimpleMessageRegist(emailRequestDTO.email());

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.createSuccessNoContent("성공"));
    }

    // 인증번호 일치 확인
    @PostMapping("/authentication/verify")
    public ResponseEntity<ApiResponse<Void>> verifyAuthenticationNumber(@Valid @RequestBody AuthenticationNumberRequestDTO authenticationNumberRequestDTO) {
        emailService.checkCertificationCode(authenticationNumberRequestDTO.authenticationNumber());

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.createSuccessNoContent("이메일 인증이 완료되었습니다."));
    }
}
