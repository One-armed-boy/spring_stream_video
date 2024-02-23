package com.stream.exception.code;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VideoExceptionCode implements ExceptionCode {
	UPLOAD_EMPTY_FILE(HttpStatus.BAD_REQUEST, "업로드 요청된 파일이 비정상적입니다. 재확인 바랍니다."),
	VIDEO_NOT_FOUND(HttpStatus.NOT_FOUND, "요청하신 비디오를 찾을 수 없습니다.");

	private final HttpStatus httpStatus;
	private final String msg;
}
