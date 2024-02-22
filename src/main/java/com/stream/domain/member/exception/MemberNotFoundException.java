package com.stream.domain.member.exception;

public class MemberNotFoundException extends RuntimeException {
	public MemberNotFoundException(String email) {
		super("Can't find member in DB (key: email, value: " + email + " )");
	}
}
