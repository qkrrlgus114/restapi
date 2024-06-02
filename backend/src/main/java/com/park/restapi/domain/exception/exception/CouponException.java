package com.park.restapi.domain.exception.exception;

import com.park.restapi.domain.exception.info.CouponExceptionInfo;
import lombok.Getter;

@Getter
public class CouponException extends RuntimeException {
    private CouponExceptionInfo exception;
    // 개발자에게 보여지는 로그
    private String log;

    public CouponException(CouponExceptionInfo exception, String log) {
        this.exception = exception;
        this.log = log;
    }
}
