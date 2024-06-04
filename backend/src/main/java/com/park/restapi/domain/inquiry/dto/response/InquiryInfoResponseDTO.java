package com.park.restapi.domain.inquiry.dto.response;

import com.park.restapi.domain.inquiry.entity.Answer;
import com.park.restapi.domain.inquiry.entity.Inquiry;
import com.park.restapi.domain.inquiry.entity.InquiryCategory;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record InquiryInfoResponseDTO(
        Long id,
        String inquiryTitle,
        String inquiryContent,
        String email,
        LocalDateTime inquiryCreateDate,
        InquiryCategory inquiryCategory,
        String answeredContent,
        LocalDateTime answeredCreateDate,
        boolean isAnswered
) {

    public static InquiryInfoResponseDTO toDTO(Answer answer, Inquiry inquiry) {
        if (answer == null) {
            return InquiryInfoResponseDTO.builder()
                    .id(inquiry.getId())
                    .inquiryTitle(inquiry.getTitle())
                    .inquiryCreateDate(inquiry.getCreatedDate())
                    .inquiryContent(inquiry.getContent())
                    .email(inquiry.getMember().getEmail())
                    .isAnswered(inquiry.isAnswered())
                    .inquiryCategory(inquiry.getInquiryCategory())
                    .build();
        }
        return InquiryInfoResponseDTO.builder()
                .id(inquiry.getId())
                .inquiryTitle(inquiry.getTitle())
                .inquiryCreateDate(inquiry.getCreatedDate())
                .inquiryContent(inquiry.getContent())
                .answeredContent(answer.getContent())
                .email(inquiry.getMember().getEmail())
                .answeredCreateDate(answer.getCreatedDate())
                .isAnswered(inquiry.isAnswered())
                .inquiryCategory(inquiry.getInquiryCategory())
                .build();
    }
}
