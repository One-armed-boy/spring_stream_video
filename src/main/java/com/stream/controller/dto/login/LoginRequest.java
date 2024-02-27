package com.stream.controller.dto.login;

import com.stream.domain.member.dto.login.LoginCommand;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class LoginRequest {
	@NotNull(message = "이메일을 입력해주세요.")
	@Email(message = "유효한 이메일을 입력해주세요.")
	private String email;

	@NotNull(message = "비밀번호를 입력해주세요.")
	private String inputPassword;

	@Builder
	public LoginRequest(String email, String inputPassword) {
		this.email = email;
		this.inputPassword = inputPassword;
	}

	public LoginCommand toCommand() {
		return LoginCommand.builder().email(email).inputPassword(inputPassword).build();
	}
}
