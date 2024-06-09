package com.park.restapi.domain.exception.exception;

import com.park.restapi.domain.exception.info.PostLikeExceptionInfo;
import lombok.Getter;

@Getter
public class PostLikeException extends RuntimeException implements CommonException {
    private PostLikeExceptionInfo exception;
    // 개발자에게 보여지는 로그
    private String log;

    public PostLikeException(PostLikeExceptionInfo exception, String log) {
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
