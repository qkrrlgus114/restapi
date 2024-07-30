package com.park.restapi.domain.exception.info;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum PostExceptionInfo {

    NOT_FOUND_POST(HttpStatus.NOT_FOUND, "P-001", "게시글을 찾을 수 없습니다.");

    private HttpStatus status;
    private String code;
    private String message;

    PostExceptionInfo(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
