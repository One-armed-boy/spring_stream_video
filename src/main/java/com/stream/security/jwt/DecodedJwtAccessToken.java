package com.stream.security.jwt;

import com.stream.domain.role.Role;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class DecodedJwtAccessToken {
	private String email;
	private Role role;

	public DecodedJwtAccessToken(String email, Role role) {
		this.email = email;
		this.role = role;
	}
}
