package com.stream.exception.code;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberExceptionCode implements ExceptionCode {
	MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "요청하신 유저를 찾을 수 없습니다."),
	INVALID_AUTH_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 인증 요청입니다. 입력값을 확인해주세요."),
	DUPLICATED_MEMBER(HttpStatus.BAD_REQUEST, "이미 존재하는 유저 이메일입니다.");

	private final HttpStatus httpStatus;
	private final String msg;
}
