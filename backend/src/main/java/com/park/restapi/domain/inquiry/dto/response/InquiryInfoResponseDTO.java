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

    private String title;

    private String inquiryContent;

    private LocalDateTime inquiryCreateDate;

    private InquiryCategory inquiryCategory;

    private String answeredContent;

    private LocalDateTime answeredCreateDate;

    private boolean isAnswered;

    @Builder
    public InquiryInfoResponseDTO(Long id, String title, String inquiryContent, LocalDateTime inquiryCreateDate, InquiryCategory inquiryCategory, String answeredContent, LocalDateTime answeredCreateDate, boolean isAnswered) {
        this.id = id;
        this.title = title;
        this.inquiryContent = inquiryContent;
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
                    .title(inquiry.getTitle())
                    .inquiryCreateDate(inquiry.getCreateDate())
                    .inquiryContent(inquiry.getContent())
                    .isAnswered(inquiry.isAnswered())
                    .inquiryCategory(inquiry.getInquiryCategory()).build();
        }
        return InquiryInfoResponseDTO.builder()
                .id(inquiry.getId())
                .title(inquiry.getTitle())
                .inquiryCreateDate(inquiry.getCreateDate())
                .inquiryContent(inquiry.getContent())
                .answeredContent(answer.getContent())
                .answeredCreateDate(answer.getCreateDate())
                .isAnswered(inquiry.isAnswered())
                .inquiryCategory(inquiry.getInquiryCategory()).build();
    }
}
