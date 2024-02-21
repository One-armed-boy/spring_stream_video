package com.stream.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.stream.domain.member.dto.signup.SignupCommand;
import com.stream.domain.member.dto.signup.SignupResult;
import com.stream.facade.SignupFacade;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@RestController
public class SignupController {
	private SignupFacade signupFacade;

	@Autowired
	public SignupController(SignupFacade signupFacade) {
		this.signupFacade = signupFacade;
	}

	@PostMapping(path = "/sign-up")
	public ResponseEntity<SignupResponse> signup(@Valid @RequestBody final SignupRequest requestBody) {
		SignupResult result = this.signupFacade.signUp(requestBody.toCommand());
		return ResponseEntity.ok().body(new SignupResponse(result.getEmail(), result.getSignupDate()));
	}

	@Builder
	@Getter
	public static class SignupRequest {
		@NotNull(message = "이메일을 입력해주세요.")
		@Email(message = "유효한 이메일을 입력해주세요.")
		private String email;

		@NotNull(message = "비밀번호를 입력해주세요.")
		private String inputPassword;

		public SignupCommand toCommand() {
			return SignupCommand.builder().email(this.email).password(this.inputPassword).build();
		}
	}

	@AllArgsConstructor
	public static class SignupResponse {
		private String email;
		private Date signupDate;
	}

}
