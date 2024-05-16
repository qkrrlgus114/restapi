package com.park.restapi.domain.member.controller;


import com.park.restapi.domain.member.dto.request.CertificationCodeRequestDTO;
import com.park.restapi.domain.member.dto.request.EmailRequestDTO;
import com.park.restapi.domain.member.service.EmailService;
import com.park.restapi.domain.member.service.impl.EmailServiceImpl;
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
@RequestMapping("/api")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    // 인증번호 전송
    @PostMapping ("authentications/send")
    public ResponseEntity<ApiResponse<?>> createCretificationNumber(@Valid @RequestBody EmailRequestDTO dto) throws Exception {
        emailService.sendSimpleMessageRegist(dto.getEmail());

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.createSuccessNoContent("성공"));
    }

    // 인증번호 일치 확인
    @PostMapping("authentications/verify")
    public ResponseEntity<?> verifyCertificationNumber(@Valid @RequestBody CertificationCodeRequestDTO dto) {
        emailService.checkCertificationCode(dto.getCertificationCode());

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.createSuccessNoContent("인증 성공"));
    }
}
