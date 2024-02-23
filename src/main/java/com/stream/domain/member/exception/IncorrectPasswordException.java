package com.stream.domain.member.exception;

import com.stream.exception.BaseException;
import com.stream.exception.code.MemberExceptionCode;

public class IncorrectPasswordException extends BaseException {
	public IncorrectPasswordException() {
		super(MemberExceptionCode.INVALID_AUTH_REQUEST);
	}
}
