package com.stream.facade;

import java.util.Date;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.stream.domain.member.Member;
import com.stream.domain.member.MemberService;
import com.stream.domain.member.dto.login.LoginCommand;
import com.stream.domain.member.dto.signup.SignupCommand;
import com.stream.domain.member.exception.IncorrectPasswordException;

@SpringBootTest
public class LoginFacadeTest {
	private LoginFacade loginFacade;
	private SignupFacade signupFacade;
	private MemberService memberService;

	@Autowired
	public LoginFacadeTest(LoginFacade loginFacade, SignupFacade signupFacade, MemberService memberService) {
		this.loginFacade = loginFacade;
		this.signupFacade = signupFacade;
		this.memberService = memberService;
	}

	@Test
	@DisplayName("회원 가입 수행 > 해당 계정 로그인 > 성공")
	void loginSuccess() {
		// given
		String mockEmail = "abdef@test.com";
		String inputPwd = "1q2w3e4r";
		this.signupFacade.signUp(SignupCommand.builder().email(mockEmail).password(inputPwd).build());

		Member memberBeforeLogin = this.memberService.getMemberByEmail(mockEmail);
		Assertions.assertThat(memberBeforeLogin.getLastLoginAt()).isNull();

		LoginCommand command = LoginCommand.builder().email(mockEmail).inputPassword(inputPwd).build();

		// when
		Date beforeTest = new Date();
		this.loginFacade.login(command);

		// then
		Member member = this.memberService.getMemberByEmail(mockEmail);
		Assertions.assertThat(member.getLastLoginAt()).isCloseTo(beforeTest, 5000);
	}

	@Test
	@DisplayName("회원 가입 수행 > 잘못된 비밀번호를 통한 로그인 요청 > 예외 발생")
	void loginFailBecauseOfIncorrectPwd() {
		// given
		String mockEmail = "abcdef@test.com";
		String mockPwd = "1q2w3e4r";
		this.signupFacade.signUp(SignupCommand.builder().email(mockEmail).password(mockPwd).build());

		String incorrectPwd = "abcdefg";

		LoginCommand command = LoginCommand.builder().email(mockEmail).inputPassword(incorrectPwd).build();

		Assertions.assertThatThrownBy(() -> {
			// when
			this.loginFacade.login(command);
			// then
		}).isInstanceOf(IncorrectPasswordException.class);
	}

	@Test
	@DisplayName("회원 가입 미 수행 > 해당 정보를 통해 로그인 요청 > 예외 발생")
	void loginFailBecauseOfNotSignup() {
		// given
		String mockEmail = "abcdefg@test.com";
		String mockPwd = "1q2w3e4r";
		LoginCommand command = LoginCommand.builder().email(mockEmail).inputPassword(mockPwd).build();

		Assertions.assertThatThrownBy(() -> {
			// when
			this.loginFacade.login(command);
			// then
		}).isInstanceOf(RuntimeException.class);
	}
}
