package com.park.restapi.domain.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignUpRequestDTO {

	@NotBlank(message = "이메일을 입력해주세요.")
	@Email(message = "이메일 형식이 아닙니다.")
	private String email;

	@NotBlank(message = "비밀번호를 입력해주세요.")
	@Size(min = 8, max = 15, message = "비밀번호는 8자 이상 15자 이하로 입력해주세요.")
	private String password;

	@NotBlank(message = "닉네임을 입력해주세요.")
	@Pattern(regexp = "^[a-zA-Z가-힣0-9]*$", message = "닉네임은 한글, 영문, 숫자만 입력 가능합니다.")
	@Size(min = 3, max = 10, message = "닉네임은 3자 이상 10자 이하로 입력해주세요.")
	private String nickname;

	@Builder
	public SignUpRequestDTO(String email, String password, String nickname) {
		this.email = email;
		this.password = password;
		this.nickname = nickname;
	}
}
