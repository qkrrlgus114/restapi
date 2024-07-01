package com.park.restapi.domain.inquiry.service;

import com.park.restapi.domain.inquiry.dto.request.InquiryRequestDTO;
import com.park.restapi.domain.inquiry.dto.response.InquiryInfoResponseDTO;
import com.park.restapi.domain.inquiry.dto.response.InquiryListResponseDTO;

public interface InquiryService {
    // 문의 등록하기
    void inquiryRegister(InquiryRequestDTO inquiryRequestDTO);

    // 내 모든 문의 가져오기
    InquiryListResponseDTO getMyInquiries(int page);

    // 특정 상세 문의 내역 가져오기
    InquiryInfoResponseDTO getTargetInquiry(Long inquiryId);
}
