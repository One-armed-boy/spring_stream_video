package com.stream.exception.code;

import org.springframework.http.HttpStatus;

import com.stream.exception.ErrorResponse;

public interface ExceptionCode {
	String name();

	HttpStatus getHttpStatus();

	String getMsg();

	default ErrorResponse toErrResponse() {
		return ErrorResponse.builder().code(name()).msg(getMsg()).build();
	}
}
