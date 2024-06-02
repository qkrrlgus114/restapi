package com.park.restapi.domain.exception.exception;

import com.park.restapi.domain.exception.info.MemberExceptionInfo;
import lombok.Getter;

@Getter
public class MemberException extends RuntimeException{
    private MemberExceptionInfo exception;
    // 개발자에게 보여지는 로그
    private String log;

    public MemberException(MemberExceptionInfo exception, String log) {
        this.exception = exception;
        this.log = log;
    }
}
