package com.stream.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.stream.controller.dto.signup.SignupRequest;
import com.stream.controller.dto.signup.SignupResponse;
import com.stream.domain.member.dto.signup.SignupResult;
import com.stream.facade.SignupFacade;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class SignupController {
	private final SignupFacade signupFacade;

	@Autowired
	public SignupController(SignupFacade signupFacade) {
		this.signupFacade = signupFacade;
	}

	@PostMapping(path = "/sign-up")
	public ResponseEntity<SignupResponse> signup(@Valid @RequestBody final SignupRequest requestBody) {
		SignupResult result = signupFacade.signUp(requestBody.toCommand());

		log.debug("Sign up Success! (email: " + result.getEmail() + " )");

		return ResponseEntity.ok().body(new SignupResponse(result.getEmail(), result.getSignupDate()));
	}
}
