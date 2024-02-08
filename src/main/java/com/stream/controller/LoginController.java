package com.stream.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.stream.domain.member.dto.login.LoginCommand;
import com.stream.facade.LoginFacade;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@RestController
public class LoginController {
	private LoginFacade loginFacade;

	@Autowired
	public LoginController(LoginFacade loginFacade) {
		this.loginFacade = loginFacade;
	}

	@PostMapping(path = "/members/login")
	public ResponseEntity login(@Valid @RequestBody final LoginRequest request) {
		this.loginFacade.login(request.toCommand());
		return ResponseEntity.ok().build();
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
			return LoginCommand.builder().email(this.email).inputPassword(this.inputPassword).build();
		}
	}
}
