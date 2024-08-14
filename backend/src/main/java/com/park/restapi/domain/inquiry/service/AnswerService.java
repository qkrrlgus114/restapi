package com.park.restapi.domain.inquiry.service;

import com.park.restapi.domain.inquiry.dto.request.AnswerRequestDTO;
import com.park.restapi.domain.inquiry.entity.Inquiry;

public interface AnswerService {

    // 답변 등록
    Inquiry createAnswer(Long inquiryId, AnswerRequestDTO requestDTO);

    // 답변 수정
    void updateAnswer(Long inquiryId, AnswerRequestDTO requestDTO);
}
