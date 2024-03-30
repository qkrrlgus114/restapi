package com.park.restapi.domain.exception.info;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum UserExceptionInfo {
    EXIST_USER_SIGNUP_DATA(HttpStatus.BAD_REQUEST, "1001", "이미 존재하는 유저 정보입니다."),
    FAIL_LOGIN(HttpStatus.UNAUTHORIZED, "1002", "로그인에 실패하였습니다."),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "1003", "유저를 찾을 수 없습니다"),
    NO_REMAINING_USES(HttpStatus.BAD_REQUEST, "1004", "토큰이 부족합니다."),
    ALREADY_GET_COUPON(HttpStatus.BAD_REQUEST, "1005", "오늘 이미 쿠폰을 획득하셨습니다.");


    private HttpStatus status;
    private String code;
    private String message;

    UserExceptionInfo(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
