package com.park.restapi.domain.exception.exception;

import com.park.restapi.domain.exception.info.MemberExceptionInfo;
import lombok.Getter;

@Getter
public class MemberException extends RuntimeException{
    private MemberExceptionInfo exception;
    private String log;

    public MemberException(MemberExceptionInfo exception, String log) {
        this.exception = exception;
        this.log = log;
    }
}
