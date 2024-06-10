package com.park.restapi.domain.exception.exception;

import com.park.restapi.domain.exception.info.InquiryExceptionInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InquiryException extends RuntimeException implements CommonException {
    private InquiryExceptionInfo exception;
    // 개발자에게 보여지는 로그
    private String log;

    @Override
    public String getExceptionMessage() {
        return exception.getMessage();
    }
}
