package com.park.restapi.domain.exception.exception;

import com.park.restapi.domain.exception.info.AnswerExceptionInfo;
import lombok.Getter;

@Getter
public class AnswerException extends RuntimeException implements CommonException {
    private AnswerExceptionInfo exception;
    // 개발자에게 보여지는 로그
    private String log;

    public AnswerException(AnswerExceptionInfo exception, String log) {
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
