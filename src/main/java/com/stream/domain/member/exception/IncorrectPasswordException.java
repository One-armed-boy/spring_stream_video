package com.stream.domain.member.exception;

public class IncorrectPasswordException extends RuntimeException {
	public IncorrectPasswordException() {
		super("Input password is not corrected.");
	}
}
