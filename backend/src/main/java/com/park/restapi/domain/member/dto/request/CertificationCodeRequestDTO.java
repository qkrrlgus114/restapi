package com.park.restapi.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CertificationCodeRequestDTO {

	@NotBlank(message = "인증번호를 입력해주세요.")
	private String certificationCode;
}
