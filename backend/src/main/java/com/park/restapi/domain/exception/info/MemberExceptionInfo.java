package com.park.restapi.domain.exception.info;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum MemberExceptionInfo {
    EXIST_MEMBER_SIGNUP_DATA(HttpStatus.BAD_REQUEST, "M-001", "이미 존재하는 유저 정보입니다."),
    FAIL_LOGIN(HttpStatus.UNAUTHORIZED, "M-002", "로그인에 실패하였습니다."),
    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "M-003", "유저를 찾을 수 없습니다."),
    NO_REMAINING_USES(HttpStatus.BAD_REQUEST, "M-004", "토큰이 부족합니다."),
    ALREADY_GET_COUPON(HttpStatus.BAD_REQUEST, "M-005", "이미 오늘의 쿠폰을 획득하셨습니다."),
    NOT_FOUND_REFRESH_TOKEN(HttpStatus.NOT_FOUND, "M-006", "리프레시 토큰을 찾을 수 없습니다."),
    BANNED_MEMBER(HttpStatus.BAD_REQUEST, "M-007", "이용이 불가능한 계정입니다."),
    WITHDRAWAL_MEMBER(HttpStatus.BAD_REQUEST, "M-008", "이용이 불가능한 계정입니다."),
    NOT_MATCH_PASSWORD(HttpStatus.BAD_REQUEST, "M-009", "비밀번호가 일치하지 않습니다."),
    NOT_WITHDRAWAL_ADMIN(HttpStatus.BAD_REQUEST, "M-010", "관리자는 계정 탈퇴가 불가능합니다."),
    USER_NOT_ADMIN(HttpStatus.FORBIDDEN, "M-011", "관리자가 아닙니다.");

    private HttpStatus status;
    private String code;
    private String message;

    MemberExceptionInfo(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
