package com.stream.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stream.controller.dto.login.LoginRequest;
import com.stream.domain.member.dto.signup.SignupCommand;
import com.stream.facade.SignupFacade;
import com.stream.security.jwt.JwtMetadata;
import com.stream.util.TestHelper;

@Import(TestHelper.class)
@SpringBootTest
@AutoConfigureMockMvc
public class LoginControllerTest {
	private final MockMvc mockMvc;
	private final TestHelper testHelper;
	private final SignupFacade signupFacade;
	private final ObjectMapper objectMapper;

	@Autowired
	public LoginControllerTest(MockMvc mockMvc, TestHelper testHelper,
		SignupFacade signupFacade, ObjectMapper objectMapper) {
		this.mockMvc = mockMvc;
		this.testHelper = testHelper;
		this.signupFacade = signupFacade;
		this.objectMapper = objectMapper;
	}

	@BeforeEach
	void initDB() {
		testHelper.initTables();
	}

	@AfterEach
	void cleanDB() {
		testHelper.clearTables();
	}

	@Test
	@DisplayName("DB 내 특정 계정 존재 X -> 해당 계정을 통해 로그인 요청 -> Not Found(404)")
	void loginFailBecauseNotSignup() throws Exception {
		// given
		var mockEmail = "testtest@test.com";
		var mockPwd = "1qndnien3u2ufnk";

		// when
		mockMvc.perform(buildLoginApiReq(mockEmail, mockPwd))
			// then
			.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	@Test
	@DisplayName("DB 내 특정 계정 존재 O -> 잘못된 패스워드를 통한 로그인 요청 -> Bad Request(400)")
	void loginFailBecauseInvalidPwd() throws Exception {
		// given
		var mockEmail = "test124@test.com";
		var mockPwd = "1q2w3e4r5t!!@@##";
		signup(mockEmail, mockPwd);

		// when
		var invalidPwd = "1q2w3e4r!!@@%%";
		mockMvc.perform(buildLoginApiReq(mockEmail, invalidPwd))
			// then
			.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	@DisplayName("DB 내 특정 계정 존재 O -> 유효한 계정 정보를 통한 로그인 요청 -> 로그인 성공")
	void loginSuccess() throws Exception {
		// given
		var mockEmail = "test@test.com";
		var mockPwd = "qwerty1234";
		signup(mockEmail, mockPwd);

		// when
		mockMvc.perform(buildLoginApiReq(mockEmail, mockPwd))
			// then
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
			.andExpect(MockMvcResultMatchers.cookie().exists(JwtMetadata.ACCESS_TOKEN_KEY));
	}

	private RequestBuilder buildLoginApiReq(String email, String password) throws Exception {
		String content = objectMapper.writeValueAsString(
			LoginRequest.builder().email(email).inputPassword(password).build());
		return MockMvcRequestBuilders.post("/login").contentType(MediaType.APPLICATION_JSON).content(content);
	}

	private void signup(String email, String password) {
		signupFacade.signUp(SignupCommand.builder().email(email).password(password).build());
	}
}
