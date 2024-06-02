package com.park.restapi.domain.inquiry.dto.response;

import com.park.restapi.domain.inquiry.entity.Inquiry;
import com.park.restapi.domain.inquiry.entity.InquiryCategory;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InquiryResponseDTO {

    private Long id;

    private String title;

    private LocalDateTime createDate;

    private InquiryCategory inquiryCategory;

    private boolean isAnswered;

    @Builder
    public InquiryResponseDTO(Long id, String title, LocalDateTime createDate, InquiryCategory inquiryCategory, boolean isAnswered) {
        this.id = id;
        this.title = title;
        this.createDate = createDate;
        this.inquiryCategory = inquiryCategory;
        this.isAnswered = isAnswered;
    }

    public static InquiryResponseDTO toDTO(Inquiry inquiry){
        return InquiryResponseDTO.builder()
                .id(inquiry.getId())
                .inquiryCategory(inquiry.getInquiryCategory())
                .isAnswered(inquiry.isAnswered())
                .title(inquiry.getTitle())
                .createDate(inquiry.getCreateDate()).build();
    }
}
