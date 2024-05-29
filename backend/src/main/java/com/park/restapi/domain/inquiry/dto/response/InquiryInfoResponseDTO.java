package com.park.restapi.domain.inquiry.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.park.restapi.domain.inquiry.entity.Answer;
import com.park.restapi.domain.inquiry.entity.Inquiry;
import com.park.restapi.domain.inquiry.entity.InquiryCategory;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InquiryInfoResponseDTO {

    private Long id;

    private String inquiryTitle;

    private String inquiryContent;

    private String email;

    private LocalDateTime inquiryCreateDate;

    private InquiryCategory inquiryCategory;

    private String answeredContent;

    private LocalDateTime answeredCreateDate;

    private boolean isAnswered;

    @Builder
    public InquiryInfoResponseDTO(Long id, String inquiryTitle, String inquiryContent, String email, LocalDateTime inquiryCreateDate, InquiryCategory inquiryCategory, String answeredContent, LocalDateTime answeredCreateDate, boolean isAnswered) {
        this.id = id;
        this.inquiryTitle = inquiryTitle;
        this.inquiryContent = inquiryContent;
        this.email = email;
        this.inquiryCreateDate = inquiryCreateDate;
        this.inquiryCategory = inquiryCategory;
        this.answeredContent = answeredContent;
        this.answeredCreateDate = answeredCreateDate;
        this.isAnswered = isAnswered;
    }

    public static InquiryInfoResponseDTO toDTO(Answer answer, Inquiry inquiry) {
        if (answer == null) {
            return InquiryInfoResponseDTO.builder()
                    .id(inquiry.getId())
                    .inquiryTitle(inquiry.getTitle())
                    .inquiryCreateDate(inquiry.getCreateDate())
                    .inquiryContent(inquiry.getContent())
                    .email(inquiry.getMember().getEmail())
                    .isAnswered(inquiry.isAnswered())
                    .inquiryCategory(inquiry.getInquiryCategory()).build();
        }
        return InquiryInfoResponseDTO.builder()
                .id(inquiry.getId())
                .inquiryTitle(inquiry.getTitle())
                .inquiryCreateDate(inquiry.getCreateDate())
                .inquiryContent(inquiry.getContent())
                .answeredContent(answer.getContent())
                .email(inquiry.getMember().getEmail())
                .answeredCreateDate(answer.getCreateDate())
                .isAnswered(inquiry.isAnswered())
                .inquiryCategory(inquiry.getInquiryCategory()).build();
    }
}
