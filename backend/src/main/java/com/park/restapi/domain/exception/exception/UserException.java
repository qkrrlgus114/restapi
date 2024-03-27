package com.park.restapi.domain.exception.exception;

import com.park.restapi.domain.exception.info.UserExceptionInfo;
import lombok.Getter;

@Getter
public class UserException extends RuntimeException{
    private UserExceptionInfo exception;
    private String log;

    public UserException(UserExceptionInfo exception, String log) {
        this.exception = exception;
        this.log = log;
    }
}
