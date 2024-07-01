package com.park.restapi.domain.exception.exception;

import com.park.restapi.domain.exception.info.MemberExceptionInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class MemberException extends RuntimeException implements CommonException {
    private MemberExceptionInfo exception;
    // 개발자에게 보여지는 로그
    private String log;

    @Override
    public String getExceptionMessage() {
        return exception.getMessage();
    }

    @Override
    public HttpStatus getStatus() {
        return exception.getStatus();
    }

    @Override
    public String getCode() {
        return exception.getCode();
    }
}
