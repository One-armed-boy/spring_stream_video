package com.stream.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.stream.domain.member.exception.DuplicatedMemberCreateException;
import com.stream.domain.member.exception.IncorrectPasswordException;
import com.stream.domain.member.exception.MemberNotFoundException;
import com.stream.domain.role.exception.RoleNotFoundException;
import com.stream.domain.video.exception.EmptyFileUploadException;
import com.stream.domain.video.exception.VideoNotFoundException;
import com.stream.exception.code.CommonExceptionCode;
import com.stream.exception.code.ExceptionCode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	@ExceptionHandler({
		// Member
		DuplicatedMemberCreateException.class,
		IncorrectPasswordException.class,
		MemberNotFoundException.class,

		// Role
		RoleNotFoundException.class,

		// Video
		EmptyFileUploadException.class,
		VideoNotFoundException.class
	})
	protected ResponseEntity<ErrorResponse> handleBaseException(final BaseException e) {
		logError(e);
		return createResponse(e.getExceptionCode());
	}

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<ErrorResponse> handleAnotherException(final Exception e) {
		logError(e);
		ExceptionCode exceptionCode = CommonExceptionCode.INTERNAL_SERVER_ERROR;
		return createResponse(exceptionCode);
	}

	private void logError(final Exception e) {
		log.error(e.getClass().getName(), e);
	}

	private ResponseEntity<ErrorResponse> createResponse(ExceptionCode exceptionCode) {
		return ResponseEntity.status(exceptionCode.getHttpStatus()).body(exceptionCode.toErrResponse());
	}
}
