package com.park.restapi.domain.inquiry.service;

import com.park.restapi.domain.inquiry.dto.request.AnswerRequestDTO;

public interface AnswerService {

    // 답변 등록
    void createAnswer(AnswerRequestDTO requestDTO);

    // 답변 수정
    void updateAnswer(AnswerRequestDTO requestDTO);
}
