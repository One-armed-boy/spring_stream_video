package com.stream.facade;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stream.domain.member.Member;
import com.stream.domain.member.MemberService;
import com.stream.domain.member.dto.login.LoginCommand;
import com.stream.domain.member.dto.login.LoginResult;
import com.stream.domain.member.exception.IncorrectPasswordException;
import com.stream.security.jwt.DecodedJwtAccessToken;
import com.stream.security.jwt.JwtManager;
import com.stream.security.jwt.JwtMetadata;

@Service
public class LoginFacade {
	private MemberService memberService;
	private PasswordEncoder passwordEncoder;
	private JwtManager jwtManager;

	@Autowired
	public LoginFacade(MemberService memberService, PasswordEncoder passwordEncoder, JwtManager jwtManager) {
		this.memberService = memberService;
		this.passwordEncoder = passwordEncoder;
		this.jwtManager = jwtManager;
	}

	@Transactional
	public LoginResult login(LoginCommand command) {
		String email = command.getEmail();
		String inputPassword = command.getInputPassword();

		Member member = memberService.getMemberByEmail(email);

		if (!passwordEncoder.matches(inputPassword, member.getPassword())) {
			throw new IncorrectPasswordException();
		}
		member.login(new Date());
		return new LoginResult(
			jwtManager.issueToken(new DecodedJwtAccessToken(member.getEmail(), member.getRole())),
			JwtMetadata.ACCESS_TOKEN_EXPIRE_MS);
	}
}
