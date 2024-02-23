package com.stream.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ErrorResponse {
	private String code;
	private String msg;

	@Builder
	public ErrorResponse(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}
}
