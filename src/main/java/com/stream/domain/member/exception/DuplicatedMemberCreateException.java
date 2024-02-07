package com.stream.domain.member.exception;

public class DuplicatedMemberCreateException extends RuntimeException {
	public DuplicatedMemberCreateException() {
		super("Exception be cause of request to create duplicated member");
	}
}
