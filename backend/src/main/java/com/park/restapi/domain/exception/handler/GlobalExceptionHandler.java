package com.park.restapi.domain.exception.handler;

import com.park.restapi.domain.exception.exception.*;
import com.park.restapi.util.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // 이메일 예외처리
    @ExceptionHandler(EmailException.class)
    public ResponseEntity<ApiResponse<Void>> handleEmailException(EmailException e, HttpServletRequest request) {
        log.warn("요청 실패 - 요청 경로 : {}, 이유 : {}, 로그메시지 : {}", request.getRequestURI(), e.getException().getMessage(), e.getLog());

        return ResponseEntity.status(e.getException().getStatus()).body(ApiResponse.createError(e.getException().getCode(), e.getException().getMessage()));
    }

    // 유저 예외처리
    @ExceptionHandler(MemberException.class)
    public ResponseEntity<ApiResponse<Void>> handleUserException(MemberException e, HttpServletRequest request) {
        log.warn("요청 실패 - 요청 경로 : {}, 이유 : {}, 로그메시지 : {}", request.getRequestURI(), e.getException().getMessage(), e.getLog());

        return ResponseEntity.status(e.getException().getStatus()).body(ApiResponse.createError(e.getException().getCode(), e.getException().getMessage()));
    }

    // GPT 예외처리
    @ExceptionHandler(GPTException.class)
    public ResponseEntity<ApiResponse<Void>> handleGPTException(GPTException e, HttpServletRequest request) {
        log.warn("요청 실패 - 요청 경로 : {}, 이유 : {}, 로그메시지 : {}", request.getRequestURI(), e.getException().getMessage(), e.getLog());

        return ResponseEntity.status(e.getException().getStatus()).body(ApiResponse.createError(e.getException().getCode(), e.getException().getMessage()));
    }

    // 쿠폰 예외처리
    @ExceptionHandler(CouponException.class)
    public ResponseEntity<ApiResponse<Void>> handleCouponException(CouponException e, HttpServletRequest request) {
        log.warn("요청 실패 - 요청 경로 : {}, 이유 : {}, 로그메시지 : {}", request.getRequestURI(), e.getException().getMessage(), e.getLog());

        return ResponseEntity.status(e.getException().getStatus()).body(ApiResponse.createError(e.getException().getCode(), e.getException().getMessage()));
    }

    // 문의내역 예외처리
    @ExceptionHandler(InquiryException.class)
    public ResponseEntity<ApiResponse<Void>> handleInquiryException(InquiryException e, HttpServletRequest request) {
        log.warn("요청 실패 - 요청 경로 : {}, 이유 : {}, 로그메시지 : {}", request.getRequestURI(), e.getException().getMessage(), e.getLog());

        return ResponseEntity.status(e.getException().getStatus()).body(ApiResponse.createError(e.getException().getCode(), e.getException().getMessage()));
    }

    // 문의내역 답변 예외처리
    @ExceptionHandler(AnswerException.class)
    public ResponseEntity<ApiResponse<Void>> handleAnswerException(AnswerException e, HttpServletRequest request) {
        log.warn("요청 실패 - 요청 경로 : {}, 이유 : {}, 로그메시지 : {}", request.getRequestURI(), e.getException().getMessage(), e.getLog());

        return ResponseEntity.status(e.getException().getStatus()).body(ApiResponse.createError(e.getException().getCode(), e.getException().getMessage()));
    }

    // Validated 유효성 검사 실패
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleConstraintViolationException(ConstraintViolationException e) {

        String errorMessage = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .findFirst()
                .orElse("");
        log.error("유효성 검사 실패 예외 발생 : ", errorMessage);

        return ResponseEntity.status(400).body(ApiResponse.createClientError(errorMessage));
    }

    // Valid 유효성 검사 실패
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        String errorMessage = e.getBindingResult().getAllErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .findFirst() // 스트림에서 첫 번째 요소만 가져옴
                .orElse("유효성 검사 오류 발생");

        log.error("유효성 검사 실패 예외 발생 : ", errorMessage);

        return ResponseEntity.status(400).body(ApiResponse.createClientError(errorMessage));
    }
}
