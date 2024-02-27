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
import com.stream.util.TestHelper;

@Import(TestHelper.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SignupControllerTest {
	private final MockMvc mockMvc;
	private final TestHelper testHelper;

	@Autowired
	public SignupControllerTest(MockMvc mockMvc, TestHelper testHelper) {
		this.mockMvc = mockMvc;
		this.testHelper = testHelper;
	}

	@BeforeEach
	void initDB() {
		testHelper.initDB();
	}

	@AfterEach
	void clearDB() {
		testHelper.clearTables();
	}

	@Test
	@DisplayName("서비스 내 멤버 X -> 신규 멤버 가입 요청 -> 성공 반환")
	void signupSuccess() throws Exception {
		// Given
		String mockEmail = "test@test.com";
		String mockPwd = "1q2w3e4r5t6y!!";

		// When
		mockMvc.perform(buildSignupApiReq(mockEmail, mockPwd))
			// Then
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

	@Test
	@DisplayName("서비스 내 특정 Member 존재 -> 동일 멤버 가입 요청 -> Bad Request(400) 반환")
	void signupFailBecauseOfDuplicatedMember() throws Exception {
		// Given
		String mockEmail = "test2@test.com";
		String mockPwd = "1q2w3e4r5t6y!!";
		mockMvc.perform(buildSignupApiReq(mockEmail, mockPwd));

		// When
		mockMvc.perform(buildSignupApiReq(mockEmail, mockPwd))
			// Then
			.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	private RequestBuilder buildSignupApiReq(String email, String password) throws Exception {
		String content = new ObjectMapper().writeValueAsString(
			SignupController.SignupRequest.builder().email(email).inputPassword(password).build());
		return MockMvcRequestBuilders.post("/sign-up")
			.contentType(MediaType.APPLICATION_JSON)
			.content(content);
	}
}
