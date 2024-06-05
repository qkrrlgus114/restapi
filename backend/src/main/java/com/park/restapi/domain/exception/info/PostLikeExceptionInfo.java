package com.park.restapi.domain.exception.info;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum PostLikeExceptionInfo {

    ALREADY_LIKE_POST(HttpStatus.BAD_REQUEST, "PL-001", "이미 좋아요를 누르셨습니다."),
    DO_NOT_LIKE_POST(HttpStatus.BAD_REQUEST, "PL-002", "좋아요를 누르지 않으셨습니다."),
    DO_NOT_SELF_LIKE(HttpStatus.BAD_REQUEST, "PL-003", "본인의 게시글은 좋아요를 누를 수 없습니다.");

    private HttpStatus status;
    private String code;
    private String message;

    PostLikeExceptionInfo(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
