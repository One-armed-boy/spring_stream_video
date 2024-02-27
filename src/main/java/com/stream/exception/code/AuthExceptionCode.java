package com.stream.exception.code;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthExceptionCode implements ExceptionCode {
	TOKEN_IS_NOT_FOUND(HttpStatus.FORBIDDEN, "재 로그인이 필요합니다. 쿠키 내 인증 토큰을 찾을 수 없습니다.");

	private final HttpStatus httpStatus;
	private final String msg;
}
