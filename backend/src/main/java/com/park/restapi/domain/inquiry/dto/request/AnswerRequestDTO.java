package com.park.restapi.domain.inquiry.dto.request;

import com.park.restapi.domain.inquiry.entity.Answer;
import com.park.restapi.domain.inquiry.entity.Inquiry;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnswerRequestDTO {

    @NotNull(message = "내용은 필수입니다.")
    @Size(min = 10, max = 2000, message = "답변은 최소 10자 최대 2000자까지 가능합니다.")
    private String content;

    @NotNull(message = "문의 번호가 없습니다.")
    private Long inquiryId;

    public Answer toEntity() {
        return Answer.builder()
                .content(content).build();
    }

}
