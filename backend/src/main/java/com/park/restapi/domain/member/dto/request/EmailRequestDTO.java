package com.park.restapi.domain.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EmailRequestDTO {

	@Email(message = "이메일 형식이 아닙니다.")
	@NotBlank(message = "이메일을 입력해주세요.")
	private String email;

}
