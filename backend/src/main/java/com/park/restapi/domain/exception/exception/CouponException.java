package com.park.restapi.domain.exception.exception;

import com.park.restapi.domain.exception.info.CouponExceptionInfo;
import lombok.Getter;

@Getter
public class CouponException extends RuntimeException{
    private CouponExceptionInfo exception;
    private String log;

    public CouponException(CouponExceptionInfo exception, String log) {
        this.exception = exception;
        this.log = log;
    }
}
