package com.stream.domain.member.dto.signup;

import java.util.Date;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SignupResult {
	private String email;
	private Date signupDate;
}
