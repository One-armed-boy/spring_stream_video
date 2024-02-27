package com.stream.controller;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.stream.domain.member.dto.login.LoginCommand;
import com.stream.domain.member.dto.login.LoginResult;
import com.stream.facade.LoginFacade;
import com.stream.security.jwt.JwtMetadata;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class LoginController {
	private final LoginFacade loginFacade;

	@Autowired
	public LoginController(LoginFacade loginFacade) {
		this.loginFacade = loginFacade;
	}

	@PostMapping(path = "/login")
	public ResponseEntity<LoginResponse> login(@Valid @RequestBody final LoginRequest request) {
		LoginResult result = loginFacade.login(request.toCommand());

		log.debug("Login Success! (email: " + request.getEmail() + " )");

		return ResponseEntity.ok()
			.header(HttpHeaders.SET_COOKIE, createCookie(result).toString())
			.body(new LoginResponse(result.getAccessToken()));
	}

	private ResponseCookie createCookie(LoginResult result) {
		return ResponseCookie.from(JwtMetadata.ACCESS_TOKEN_KEY, result.getAccessToken())
			.httpOnly(true)
			.secure(true)
			.maxAge(
				Duration.ofMillis(result.getAccessTokenExpireMs()))
			.build();
	}

	@Getter
	@ToString
	public static class LoginRequest {
		@NotNull(message = "이메일을 입력해주세요.")
		@Email(message = "유효한 이메일을 입력해주세요.")
		private String email;

		@NotNull(message = "비밀번호를 입력해주세요.")
		private String inputPassword;

		@Builder
		public LoginRequest(String email, String inputPassword) {
			this.email = email;
			this.inputPassword = inputPassword;
		}

		public LoginCommand toCommand() {
			return LoginCommand.builder().email(email).inputPassword(inputPassword).build();
		}
	}

	@Getter
	@ToString
	public static class LoginResponse {
		private String accessToken;

		public LoginResponse(String accessToken) {
			this.accessToken = accessToken;
		}
	}
}
