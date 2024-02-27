package com.stream.controller.dto.login;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class LoginResponse {
	private String accessToken;

	public LoginResponse(String accessToken) {
		this.accessToken = accessToken;
	}
}
