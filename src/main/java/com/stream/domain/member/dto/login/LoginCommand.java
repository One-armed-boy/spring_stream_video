package com.stream.domain.member.dto.login;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LoginCommand {
	private String email;
	private String inputPassword;
}
