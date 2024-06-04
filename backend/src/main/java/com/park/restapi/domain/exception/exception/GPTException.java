package com.park.restapi.domain.exception.exception;

import com.park.restapi.domain.exception.info.GPTExceptionInfo;
import lombok.Getter;

@Getter
public class GPTException extends RuntimeException {
    private GPTExceptionInfo exception;
    // 개발자에게 보여지는 로그
    private String log;

    public GPTException(GPTExceptionInfo exception, String log) {
        this.exception = exception;
        this.log = log;
    }
}
