package com.park.restapi.domain.exception.exception;

import org.springframework.http.HttpStatus;

public interface CommonException {
    String getExceptionMessage();

    String getLog();

    HttpStatus getStatus();

    String getCode();
}
