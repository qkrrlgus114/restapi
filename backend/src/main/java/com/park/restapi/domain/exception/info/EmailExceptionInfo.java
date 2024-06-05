package com.park.restapi.domain.exception.info;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum EmailExceptionInfo {

    FAIL_SEND_CERTIFICATION_CODE(HttpStatus.BAD_REQUEST, "E-001", "인증번호 전송에 실패하였습니다."),
    NO_MATCH_CERTIFICATION_CODE(HttpStatus.BAD_REQUEST, "E-002", "인증번호가 일치하지 않습니다"),
    ALREADY_EXPIRED_CERTIFICATION_CODE(HttpStatus.NOT_FOUND, "E-003", "인증번호가 만료되었습니다."),
    ALREADY_SIGN_UP_EMAIL(HttpStatus.BAD_REQUEST, "E-004", "이미 가입된 이메일입니다.");

    private HttpStatus status;
    private String code;
    private String message;

    EmailExceptionInfo(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
