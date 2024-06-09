package com.park.restapi.domain.exception.exception;

import com.park.restapi.domain.exception.info.EmailExceptionInfo;
import lombok.Getter;

@Getter
public class EmailException extends RuntimeException implements CommonException {
    private EmailExceptionInfo exception;
    // 개발자에게 보여지는 로그
    private String log;

    public EmailException(EmailExceptionInfo exception, String log) {
        this.exception = exception;
        this.log = log;
    }

    @Override
    public String getLog() {
        return log;
    }

    @Override
    public String getExceptionMessage() {
        return exception.getMessage();
    }
}
