package com.park.restapi.domain.inquiry.dto.response;

import com.park.restapi.domain.inquiry.entity.Inquiry;
import com.park.restapi.domain.inquiry.entity.InquiryCategory;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record InquiryResponseDTO(
        Long id,
        String title,
        LocalDateTime createDate,
        InquiryCategory inquiryCategory,
        boolean isAnswered
) {
    public static InquiryResponseDTO toDTO(Inquiry inquiry) {
        return InquiryResponseDTO.builder()
                .id(inquiry.getId())
                .inquiryCategory(inquiry.getInquiryCategory())
                .isAnswered(inquiry.isAnswered())
                .title(inquiry.getTitle())
                .createDate(inquiry.getCreateDate())
                .build();
    }
}
