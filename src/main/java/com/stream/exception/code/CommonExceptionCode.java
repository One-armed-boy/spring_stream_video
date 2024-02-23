package com.stream.exception.code;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommonExceptionCode implements ExceptionCode {
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 측에서 문제가 발생했습니다.");

	private final HttpStatus httpStatus;
	private final String msg;
}
