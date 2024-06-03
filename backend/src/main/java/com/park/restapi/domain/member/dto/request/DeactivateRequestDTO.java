package com.park.restapi.domain.member.dto.request;

import com.park.restapi.domain.member.entity.SocialType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DeactivateRequestDTO(
        @NotNull
        SocialType socialType,
        @NotBlank(message = "비밀번호를 입력해주세요.")
        String password
) {
}
