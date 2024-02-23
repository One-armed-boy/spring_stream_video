package com.stream.domain.member.dto.login;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class LoginResult {
	private String accessToken;
	private int accessTokenExpireMs;

	public LoginResult(String accessToken, int accessTokenExpireMs) {
		this.accessToken = accessToken;
		this.accessTokenExpireMs = accessTokenExpireMs;
	}
}
