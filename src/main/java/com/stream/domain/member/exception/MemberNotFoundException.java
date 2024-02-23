package com.stream.domain.member.exception;

import com.stream.exception.BaseException;
import com.stream.exception.code.MemberExceptionCode;

public class MemberNotFoundException extends BaseException {
	public MemberNotFoundException(String email) {
		super(MemberExceptionCode.MEMBER_NOT_FOUND, "Can't find member in DB (key: email, value: " + email + " )");
	}
}
