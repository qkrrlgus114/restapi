package com.park.restapi.domain.exception.exception;

import com.park.restapi.domain.exception.info.InquiryExceptionInfo;
import lombok.Getter;

@Getter
public class InquiryException extends RuntimeException{
    private InquiryExceptionInfo exception;
    // 개발자에게 보여지는 로그
    private String log;

    public InquiryException(InquiryExceptionInfo exception, String log) {
        this.exception = exception;
        this.log = log;
    }
}
