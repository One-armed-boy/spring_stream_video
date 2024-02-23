package com.stream.domain.member.dto.signup;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SignupCommand {
	private String email;
	private String password;

	@Builder
	public SignupCommand(String email, String password) {
		this.email = email;
		this.password = password;
	}
}
