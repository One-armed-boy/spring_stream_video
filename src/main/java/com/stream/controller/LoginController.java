package com.stream.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.stream.domain.member.dto.login.LoginCommand;
import com.stream.domain.member.dto.login.LoginResult;
import com.stream.facade.LoginFacade;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@RestController
public class LoginController {
	private LoginFacade loginFacade;

	@Autowired
	public LoginController(LoginFacade loginFacade) {
		this.loginFacade = loginFacade;
	}

	@PostMapping(path = "/login")
	public ResponseEntity<LoginResponse> login(@Valid @RequestBody final LoginRequest request) {
		LoginResult result = loginFacade.login(request.toCommand());
		String accessToken = result.getAccessToken();
		return ResponseEntity.ok().header("Authorization", accessToken).body(new LoginResponse(accessToken));
	}

	@Builder
	@Getter
	public static class LoginRequest {
		@NotNull(message = "이메일을 입력해주세요.")
		@Email(message = "유효한 이메일을 입력해주세요.")
		private String email;

		@NotNull(message = "비밀번호를 입력해주세요.")
		private String inputPassword;

		public LoginCommand toCommand() {
			return LoginCommand.builder().email(email).inputPassword(inputPassword).build();
		}
	}

	@Getter
	@AllArgsConstructor
	public static class LoginResponse {
		private String accessToken;
	}
}
