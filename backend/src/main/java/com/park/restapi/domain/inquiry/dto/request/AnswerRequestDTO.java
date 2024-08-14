package com.park.restapi.domain.inquiry.dto.request;

import com.park.restapi.domain.inquiry.entity.Answer;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AnswerRequestDTO(
        @NotNull(message = "내용은 필수입니다.")
        @Size(min = 10, max = 2000, message = "답변은 최소 10자 최대 2000자까지 가능합니다.")
        String content
) {
    public Answer toEntity() {
        return Answer.builder().content(content).build();
    }

}
