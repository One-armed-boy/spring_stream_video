package com.stream.domain.member.dto.signup;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SignupCommand {
	private String email;
	private String password;
}
