package com.stream.exception;

import com.stream.exception.code.ExceptionCode;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {
	private final ExceptionCode exceptionCode;

	protected BaseException(ExceptionCode exceptionCode) {
		this.exceptionCode = exceptionCode;
	}

	protected BaseException(ExceptionCode exceptionCode, String msg) {
		super(msg);
		this.exceptionCode = exceptionCode;
	}
}
