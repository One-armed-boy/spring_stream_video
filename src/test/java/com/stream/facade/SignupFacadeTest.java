package com.stream.facade;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.stream.domain.member.Member;
import com.stream.domain.member.MemberRepository;
import com.stream.domain.member.MemberService;
import com.stream.domain.member.dto.signup.SignupCommand;
import com.stream.domain.member.dto.signup.SignupResult;
import com.stream.domain.member.exception.DuplicatedMemberCreateException;

@SpringBootTest
public class SignupFacadeTest {
	private SignupFacade signupFacade;
	private MemberService memberService;
	private MemberRepository memberRepository;
	private PasswordEncoder passwordEncoder;

	@Autowired
	public SignupFacadeTest(SignupFacade signupFacade, MemberService memberService, MemberRepository memberRepository,
		PasswordEncoder passwordEncoder) {
		this.signupFacade = signupFacade;
		this.memberService = memberService;
		this.memberRepository = memberRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@AfterEach
	void cleanDB() {
		this.memberRepository.deleteAll();
	}

	@Test
	@DisplayName("빈 DB > DB 내 존재하지 않는 이메일을 통한 회원가입 요청 > 요청 성공")
	void signupSuccess() {
		// given
		String mockEmail = "abc@test.com";
		String inputPwd = "1q2w3e4r";
		SignupCommand signupCommand = SignupCommand.builder().email(mockEmail).password(inputPwd).build();

		// when
		SignupResult result = this.signupFacade.signUp(signupCommand);

		// then
		Assertions.assertThat(result.getEmail()).isEqualTo(mockEmail);

		Member member = this.memberService.getMemberByEmail(mockEmail);
		Assertions.assertThat(member.getEmail()).isEqualTo(mockEmail);
		Assertions.assertThat(this.passwordEncoder.matches(inputPwd, member.getPassword())).isTrue();
	}

	@Test
	@DisplayName("이미 회원가입된 이메일 > 동일 이메일을 통한 회원가입 요청 > 예외 발생")
	void signupFail() {
		// given
		String mockEmail = "abc@test.com";
		String mockPwd = "abcdefg";
		this.signupFacade.signUp(SignupCommand.builder().email(mockEmail).password(mockPwd).build());

		Assertions.assertThatThrownBy(() -> {
			// when
			this.signupFacade.signUp(SignupCommand.builder().email(mockEmail).password(mockPwd).build());
			// then
		}).isInstanceOf(DuplicatedMemberCreateException.class);
	}
}
