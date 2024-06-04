package com.park.restapi.domain.exception.info;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CouponExceptionInfo {

    FAIL_COUPON_DATA(HttpStatus.INTERNAL_SERVER_ERROR, "4000", "쿠폰 에러 발생(서버)"),
    NOT_EXIST_COUPON(HttpStatus.NOT_FOUND, "4001", "쿠폰이 전부 소진되었습니다.");

    private HttpStatus status;
    private String code;
    private String message;

    CouponExceptionInfo(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
