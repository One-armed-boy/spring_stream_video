package com.stream.security.jwt.exception;

public class AccessTokenNotFoundException extends RuntimeException {
	public AccessTokenNotFoundException() {
		super("Access token is not in cookies. Plz re-login");
	}
}
