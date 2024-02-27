package com.stream.controller.dto.signup;

import java.util.Date;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SignupResponse {
	private String email;
	private Date signupDate;

	public SignupResponse(String email, Date signupDate) {
		this.email = email;
		this.signupDate = signupDate;
	}
}
