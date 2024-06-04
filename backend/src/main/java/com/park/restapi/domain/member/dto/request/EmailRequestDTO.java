package com.park.restapi.domain.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailRequestDTO(
        @Email(message = "이메일 형식이 아닙니다.")
        @NotBlank(message = "이메일을 입력해주세요.")
        String email
) {
}
