package com.park.restapi.domain.exception.exception;

import com.park.restapi.domain.exception.info.PostExceptionInfo;
import lombok.Getter;

@Getter
public class PostException extends RuntimeException implements CommonException {
    private PostExceptionInfo exception;
    // 개발자에게 보여지는 로그
    private String log;

    public PostException(PostExceptionInfo exception, String log) {
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
