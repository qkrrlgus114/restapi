package com.park.restapi.domain.exception.info;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum GPTExceptionInfo {

    FAIL_REQUEST_GPT(HttpStatus.SERVICE_UNAVAILABLE, "GPT-001", "GPT에 문제가 발생했습니다. 나중에 다시 시도해주세요"),
    FAIL_INTERRUPTED(HttpStatus.INTERNAL_SERVER_ERROR, "GPT-002", "서버 에러 발생");

    private HttpStatus status;
    private String code;
    private String message;

    GPTExceptionInfo(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
