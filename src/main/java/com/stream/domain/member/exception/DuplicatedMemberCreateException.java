package com.stream.domain.member.exception;

import com.stream.exception.BaseException;
import com.stream.exception.code.MemberExceptionCode;

public class DuplicatedMemberCreateException extends BaseException {
	public DuplicatedMemberCreateException() {
		super(MemberExceptionCode.DUPLICATED_MEMBER);
	}
}
