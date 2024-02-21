package com.stream.security.jwt;

import com.stream.domain.role.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DecodedJwtAccessToken {
	private String email;
	private Role role;
}
