package com.park.restapi.domain.inquiry.service.impl;

import com.park.restapi.domain.exception.exception.AnswerException;
import com.park.restapi.domain.exception.exception.InquiryException;
import com.park.restapi.domain.exception.info.AnswerExceptionInfo;
import com.park.restapi.domain.exception.info.InquiryExceptionInfo;
import com.park.restapi.domain.inquiry.dto.request.AnswerRequestDTO;
import com.park.restapi.domain.inquiry.entity.Answer;
import com.park.restapi.domain.inquiry.entity.Inquiry;
import com.park.restapi.domain.inquiry.repository.AnswerRepository;
import com.park.restapi.domain.inquiry.repository.InquiryRepository;
import com.park.restapi.domain.inquiry.service.AnswerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnswerServiceImpl implements AnswerService {

    private final AnswerRepository answerRepository;
    private final InquiryRepository inquiryRepository;

    // 문의내역 답변 등록하기
    @Override
    @Transactional
    public Inquiry createAnswer(Long inquiryId, AnswerRequestDTO answerRequestDTO) {
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new InquiryException(InquiryExceptionInfo.NOT_FOUND_INQUIRY, inquiryId + "번 문의 내역을 찾을 수 없습니다."));

        if (inquiry.isAnswered()) {
            throw new AnswerException(AnswerExceptionInfo.EXISTS_ANSWERED, inquiryId + "번 문의에는 이미 답변이 존재합니다.");
        }

        Answer answer = answerRequestDTO.toEntity();
        answerRepository.save(answer);
        inquiry.registerAnswer(answer);

        return inquiry;
    }

    // 문의내역 답변 수정하기
    @Override
    @Transactional
    public void updateAnswer(Long inquiryId, AnswerRequestDTO answerRequestDTO) {
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new InquiryException(InquiryExceptionInfo.NOT_FOUND_INQUIRY, inquiryId + "번 문의 내역을 찾을 수 없습니다."));

        Answer answer = inquiry.getAnswer();
        answer.updateAnswer(answerRequestDTO.content());
    }
}
