package com.stream.security.jwt.exception;

import com.stream.exception.BaseException;
import com.stream.exception.code.AuthExceptionCode;

public class AccessTokenNotFoundException extends BaseException {
	public AccessTokenNotFoundException() {
		super(AuthExceptionCode.TOKEN_IS_NOT_FOUND);
	}
}
