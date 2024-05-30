package com.park.restapi.domain.inquiry.controller;

import com.park.restapi.domain.inquiry.dto.request.AnswerRequestDTO;
import com.park.restapi.domain.inquiry.entity.Inquiry;
import com.park.restapi.domain.inquiry.service.AnswerService;
import com.park.restapi.domain.member.service.EmailService;
import com.park.restapi.util.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class AnswerController {

    private final AnswerService answerService;
    private final EmailService emailService;

    // 답변 등록
    @PostMapping("answers")
    public ResponseEntity<ApiResponse<?>> createAnswer(@RequestBody @Valid AnswerRequestDTO answerRequestDTO) throws Exception {
        Inquiry inquiry = answerService.createAnswer(answerRequestDTO);
        if (inquiry.isEmailSendCheck()) {
            emailService.sendAnsweredMessage(inquiry.getMember().getEmail(), inquiry.getTitle());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.createSuccessNoContent("문의 답변 등록이 완료되었습니다."));
    }

    // 답변 수정
    @PatchMapping("answers")
    public ResponseEntity<ApiResponse<?>> updateAnswer(@RequestBody @Valid AnswerRequestDTO answerRequestDTO) {
        answerService.updateAnswer(answerRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.createSuccessNoContent("문의 답변이 수정되었습니다."));
    }
}
