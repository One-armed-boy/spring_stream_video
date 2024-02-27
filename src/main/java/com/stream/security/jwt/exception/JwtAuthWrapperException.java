package com.stream.security.jwt.exception;

import com.stream.exception.BaseException;

public class JwtAuthWrapperException extends BaseException {
	public JwtAuthWrapperException(BaseException err) {
		super(err.getExceptionCode());
	}
}
