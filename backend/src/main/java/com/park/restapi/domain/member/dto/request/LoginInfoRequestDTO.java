package com.park.restapi.domain.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LoginInfoRequestDTO(
        @NotBlank(message = "이메일을 입력해주세요.")
        @Email(message = "이메일 형식이 아닙니다.")
        String email,
        @NotBlank(message = "비밀번호를 입력해주세요.")
        @Size(min = 8, max = 15, message = "비밀번호는 8자 이상 15자 이하로 입력해주세요.")
        String password,
        @NotNull(message = "소셜타입이 필요합니다.")
        Boolean social
) {
}
