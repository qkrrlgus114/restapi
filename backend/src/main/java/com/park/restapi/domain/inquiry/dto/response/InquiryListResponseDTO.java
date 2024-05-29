package com.park.restapi.domain.inquiry.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InquiryListResponseDTO {

    private List<InquiryResponseDTO> inquiryResponseDTOS;
    private int totalPages;
    private int currentPage;

    @Builder
    public InquiryListResponseDTO(List<InquiryResponseDTO> inquiryResponseDTOS, int totalPages, int currentPage) {
        this.inquiryResponseDTOS = inquiryResponseDTOS;
        this.totalPages = totalPages;
        this.currentPage = currentPage;
    }
}
