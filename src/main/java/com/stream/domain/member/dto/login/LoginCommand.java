package com.stream.domain.member.dto.login;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class LoginCommand {
	private String email;
	private String inputPassword;

	@Builder
	public LoginCommand(String email, String inputPassword) {
		this.email = email;
		this.inputPassword = inputPassword;
	}
}
