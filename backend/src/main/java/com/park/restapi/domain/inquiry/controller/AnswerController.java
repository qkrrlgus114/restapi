package com.park.restapi.domain.inquiry.controller;

import com.park.restapi.domain.inquiry.dto.request.AnswerRequestDTO;
import com.park.restapi.domain.inquiry.entity.Inquiry;
import com.park.restapi.domain.inquiry.service.AnswerService;
import com.park.restapi.domain.member.entity.Member;
import com.park.restapi.domain.member.service.EmailService;
import com.park.restapi.util.resolver.CurrentMember;
import com.park.restapi.util.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/answers")
@RequiredArgsConstructor
@Slf4j
public class AnswerController {

    private final AnswerService answerService;
    private final EmailService emailService;

    // 답변 등록
    @PostMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> createAnswer(@PathVariable("id") Long inquiryId,
                                                          @RequestBody @Valid AnswerRequestDTO answerRequestDTO,
                                                          @CurrentMember Member member) throws Exception {
        Inquiry inquiry = answerService.createAnswer(inquiryId, answerRequestDTO);
        if (inquiry.isEmailSendCheck()) {
            emailService.sendAnsweredMessage(inquiry.getMember().getEmail(), inquiry.getTitle());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.createSuccessNoContent("문의 답변 등록이 완료되었습니다."));
    }

    // 답변 수정
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> updateAnswer(@PathVariable("id") Long inquiryId,
                                                          @RequestBody @Valid AnswerRequestDTO answerRequestDTO,
                                                          @CurrentMember Member member) {
        answerService.updateAnswer(inquiryId, answerRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.createSuccessNoContent("문의 답변이 수정되었습니다."));
    }
}
