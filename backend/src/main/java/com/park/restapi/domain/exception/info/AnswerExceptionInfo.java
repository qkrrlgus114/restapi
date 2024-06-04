package com.park.restapi.domain.exception.info;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum AnswerExceptionInfo {

    NOT_FOUND_ANSWER(HttpStatus.NOT_FOUND, "6000", "문의 답변 내역을 찾을 수 없습니다."),
    EXISTS_ANSWERED(HttpStatus.BAD_REQUEST, "6001", "이미 답변이 존재합니다.");

    private HttpStatus status;
    private String code;
    private String message;

    AnswerExceptionInfo(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
