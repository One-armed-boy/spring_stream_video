package com.stream.facade;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.stream.domain.member.Member;
import com.stream.domain.member.MemberService;
import com.stream.domain.member.dto.signup.SignupCommand;
import com.stream.domain.member.dto.signup.SignupResult;
import com.stream.domain.member.exception.DuplicatedMemberCreateException;
import com.stream.util.TestHelper;

@Import(TestHelper.class)
@SpringBootTest
public class SignupFacadeTest {
	private final SignupFacade signupFacade;
	private final MemberService memberService;
	private final PasswordEncoder passwordEncoder;
	private final TestHelper testHelper;

	@Autowired
	public SignupFacadeTest(SignupFacade signupFacade, MemberService memberService,
		PasswordEncoder passwordEncoder, TestHelper testHelper) {
		this.signupFacade = signupFacade;
		this.memberService = memberService;
		this.passwordEncoder = passwordEncoder;
		this.testHelper = testHelper;
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
	@DisplayName("빈 DB > DB 내 존재하지 않는 이메일을 통한 회원가입 요청 > 요청 성공")
	void signupSuccess() {
		// given
		String mockEmail = "abc@test.com";
		String inputPwd = "1q2w3e4r";
		SignupCommand signupCommand = SignupCommand.builder().email(mockEmail).password(inputPwd).build();

		// when
		SignupResult result = signupFacade.signUp(signupCommand);

		// then
		Assertions.assertThat(result.getEmail()).isEqualTo(mockEmail);

		Member member = memberService.getMemberByEmail(mockEmail);
		Assertions.assertThat(member.getEmail()).isEqualTo(mockEmail);
		Assertions.assertThat(passwordEncoder.matches(inputPwd, member.getPassword())).isTrue();
	}

	@Test
	@DisplayName("이미 회원가입된 이메일 > 동일 이메일을 통한 회원가입 요청 > 예외 발생")
	void signupFail() {
		// given
		String mockEmail = "abc@test.com";
		String mockPwd = "abcdefg";
		signupFacade.signUp(SignupCommand.builder().email(mockEmail).password(mockPwd).build());

		Assertions.assertThatThrownBy(() -> {
			// when
			signupFacade.signUp(SignupCommand.builder().email(mockEmail).password(mockPwd).build());
			// then
		}).isInstanceOf(DuplicatedMemberCreateException.class);
	}
}
