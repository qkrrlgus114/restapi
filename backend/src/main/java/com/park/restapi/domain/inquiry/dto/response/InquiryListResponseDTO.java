package com.park.restapi.domain.inquiry.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record InquiryListResponseDTO(
        List<InquiryResponseDTO> inquiryResponseDTOS,
        int totalPages,
        int currentPage
) {
}
