package com.stream.facade;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stream.domain.member.Member;
import com.stream.domain.member.MemberService;
import com.stream.domain.member.dto.login.LoginCommand;
import com.stream.domain.member.exception.IncorrectPasswordException;

@Service
public class LoginFacade {
	private MemberService memberService;
	private PasswordEncoder passwordEncoder;

	@Autowired
	public LoginFacade(MemberService memberService, PasswordEncoder passwordEncoder) {
		this.memberService = memberService;
		this.passwordEncoder = passwordEncoder;
	}

	@Transactional
	public void login(LoginCommand command) {
		String email = command.getEmail();
		String inputPassword = command.getInputPassword();

		Member member = this.memberService.getMemberByEmail(email);

		if (!this.passwordEncoder.matches(inputPassword, member.getPassword())) {
			throw new IncorrectPasswordException();
		}
		member.login(new Date());
	}
}
