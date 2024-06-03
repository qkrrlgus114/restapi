package com.park.restapi.domain.inquiry.dto.request;

import com.park.restapi.domain.inquiry.entity.InquiryCategory;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record InquiryRequestDTO(
        @NotNull(message = "제목을 입력해주세요.")
        @Size(max = 30, min = 1, message = "제목은 최대 30자까지 가능합니다.")
        String title,

        @NotNull(message = "내용을 입력해주세요.")
        @Size(max = 500, min = 10, message = "내용은 최소 10자 최대 500자까지 가능합니다.")
        String content,

        @NotNull(message = "문의 타입을 선택해주세요.")
        InquiryCategory inquiryCategory,

        boolean emailSendCheck
) {
}
