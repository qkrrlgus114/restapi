package com.park.restapi.domain.exception.handler;

import com.park.restapi.domain.exception.exception.CouponException;
import com.park.restapi.domain.exception.exception.EmailException;
import com.park.restapi.domain.exception.exception.GPTException;
import com.park.restapi.domain.exception.exception.UserException;
import com.park.restapi.util.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // 이메일 예외처리
    @ExceptionHandler(EmailException.class)
    public ResponseEntity<ApiResponse<Void>> handleEmailException(EmailException e , HttpServletRequest request){
        log.warn("요청 실패 - 요청 경로 : {}, 이유 : {}, 로그메시지 : {}", request.getRequestURI(), e.getException().getMessage(), e.getLog());

        return ResponseEntity.status(e.getException().getStatus()).body(ApiResponse.createError(e.getException().getCode(), e.getException().getMessage()));
    }

    // 유저 예외처리
    @ExceptionHandler(UserException.class)
    public ResponseEntity<ApiResponse<Void>> handleUserException(UserException e , HttpServletRequest request){
        log.warn("요청 실패 - 요청 경로 : {}, 이유 : {}, 로그메시지 : {}", request.getRequestURI(), e.getException().getMessage(), e.getLog());

        return ResponseEntity.status(e.getException().getStatus()).body(ApiResponse.createError(e.getException().getCode(), e.getException().getMessage()));
    }

    // GPT 예외처리
    @ExceptionHandler(GPTException.class)
    public ResponseEntity<ApiResponse<Void>> handleGPTException(GPTException e , HttpServletRequest request){
        log.warn("요청 실패 - 요청 경로 : {}, 이유 : {}, 로그메시지 : {}", request.getRequestURI(), e.getException().getMessage(), e.getLog());

        return ResponseEntity.status(e.getException().getStatus()).body(ApiResponse.createError(e.getException().getCode(), e.getException().getMessage()));
    }

    // 쿠폰 예외처리
    @ExceptionHandler(CouponException.class)
    public ResponseEntity<ApiResponse<Void>> handleCouponException(CouponException e , HttpServletRequest request){
        log.warn("요청 실패 - 요청 경로 : {}, 이유 : {}, 로그메시지 : {}", request.getRequestURI(), e.getException().getMessage(), e.getLog());

        return ResponseEntity.status(e.getException().getStatus()).body(ApiResponse.createError(e.getException().getCode(), e.getException().getMessage()));
    }
}
