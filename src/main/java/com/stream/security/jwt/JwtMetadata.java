package com.stream.security.jwt;

public class JwtMetadata {
	private JwtMetadata() {
	}

	public static final String ACCESS_TOKEN_KEY = "accessToken";
	public static final int ACCESS_TOKEN_EXPIRE_MS = 5 * 60 * 1000;
}
