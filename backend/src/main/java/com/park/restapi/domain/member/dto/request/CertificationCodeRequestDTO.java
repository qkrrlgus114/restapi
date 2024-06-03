package com.park.restapi.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CertificationCodeRequestDTO(
        @NotBlank(message = "인증번호를 입력해주세요.")
        String certificationCode
) {
}
