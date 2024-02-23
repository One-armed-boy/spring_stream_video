package com.stream.exception.code;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoleExceptionCode implements ExceptionCode {
	ROLE_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 역할입니다.");

	private final HttpStatus httpStatus;
	private final String msg;
}
