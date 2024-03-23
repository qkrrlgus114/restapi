package com.park.restapi.domain.exception.exception;

import com.park.restapi.domain.exception.info.EmailExceptionInfo;
import lombok.Getter;

@Getter
public class EmailException extends RuntimeException{
    private EmailExceptionInfo exception;
    private String log;

    public EmailException(EmailExceptionInfo exception, String log) {
        this.exception = exception;
        this.log = log;
    }
}
