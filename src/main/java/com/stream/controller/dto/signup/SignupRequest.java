package com.stream.controller.dto.signup;

import com.stream.domain.member.dto.signup.SignupCommand;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SignupRequest {
	@NotNull(message = "이메일을 입력해주세요.")
	@Email(message = "유효한 이메일을 입력해주세요.")
	private String email;

	@NotNull(message = "비밀번호를 입력해주세요.")
	private String inputPassword;

	@Builder
	public SignupRequest(String email, String inputPassword) {
		this.email = email;
		this.inputPassword = inputPassword;
	}

	public SignupCommand toCommand() {
		return SignupCommand.builder().email(email).password(inputPassword).build();
	}
}
