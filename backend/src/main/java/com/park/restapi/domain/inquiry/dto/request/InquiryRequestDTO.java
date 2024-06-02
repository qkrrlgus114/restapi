package com.park.restapi.domain.inquiry.dto.request;

import com.park.restapi.domain.inquiry.entity.InquiryCategory;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InquiryRequestDTO {

    @NotNull(message = "제목을 입력해주세요.")
    @Size(max = 30, min = 1, message = "제목은 최대 30자까지 가능합니다.")
    private String title;

    @NotNull(message = "내용을 입력해주세요.")
    @Size(max = 500, min = 10, message = "내용은 최소 10자 최대 500자까지 가능합니다.")
    private String content;

    @NotNull(message = "문의 타입을 선택해주세요.")
    private InquiryCategory inquiryCategory;

    private boolean emailSendCheck;

    @Builder
    public InquiryRequestDTO(@NotNull(message = "제목을 입력해주세요.") String title, @NotNull(message = "내용을 입력해주세요.") String content, @NotNull(message = "문의 타입을 선택해주세요.") InquiryCategory inquiryCategory, boolean emailSendCheck) {
        this.title = title;
        this.content = content;
        this.inquiryCategory = inquiryCategory;
        this.emailSendCheck = emailSendCheck;
    }
}
