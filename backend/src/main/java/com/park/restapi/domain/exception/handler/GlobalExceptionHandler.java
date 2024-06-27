package com.park.restapi.domain.exception.handler;

import com.park.restapi.domain.exception.exception.*;
import com.park.restapi.util.response.ApiResponse;
import com.park.restapi.util.slack.RequestInfo;
import com.park.restapi.util.slack.SlackMsgService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final SlackMsgService slackMsgService;

    // 이메일 예외처리
    @ExceptionHandler({EmailException.class,
            MemberException.class,
            GPTException.class,
            CouponException.class,
            InquiryException.class,
            AnswerException.class,
            PostException.class,
            PostLikeException.class})
    public ResponseEntity<ApiResponse<Void>> handleException(CommonException e, HttpServletRequest request) {
        slackMsgService.sendMsg(e, generatedRequestInfo(request));

        log.warn("요청 실패 - 계층 : {}, 요청 경로 : {}, 이유 : {}, 로그메시지 : {}",
                e.getClass().getName(), request.getRequestURI(), e.getExceptionMessage(), e.getLog());

        return ResponseEntity.status(e.getStatus())
                .body(ApiResponse.createError(e.getCode(), e.getExceptionMessage()));
    }

    // Validated 유효성 검사 실패
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleConstraintViolationException(ConstraintViolationException e) {
        String errorMessage = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .findFirst()
                .orElse("");
        log.warn("유효성 검사 실패 예외 발생 : ", errorMessage);

        return ResponseEntity.status(400).body(ApiResponse.createClientError(errorMessage));
    }

    // Valid 유효성 검사 실패
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getAllErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .findFirst() // 스트림에서 첫 번째 요소만 가져옴
                .orElse("유효성 검사 오류 발생");

        log.warn("유효성 검사 실패 예외 발생 : ", errorMessage);

        return ResponseEntity.status(400).body(ApiResponse.createClientError(errorMessage));
    }

    private RequestInfo generatedRequestInfo(HttpServletRequest request) {
        return RequestInfo.builder()
                .requestURI(request.getRequestURI())
                .method(request.getMethod()).build();
    }

}
