package com.park.restapi.domain.exception.info;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum InquiryExceptionInfo {

    NOT_FOUND_INQUIRY(HttpStatus.NOT_FOUND, "5000", "문의 내역을 찾을 수 없습니다."),
    NOT_MATCH_MEMBER(HttpStatus.BAD_REQUEST, "5001", "본인의 문의 내역만 열람 가능합니다.");

    private HttpStatus status;
    private String code;
    private String message;

    InquiryExceptionInfo(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
