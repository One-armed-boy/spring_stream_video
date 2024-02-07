package com.stream.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.stream.domain.member.Member;
import com.stream.domain.member.MemberService;
import com.stream.domain.member.dto.CreateCommand;
import com.stream.domain.member.dto.signup.SignupCommand;
import com.stream.domain.member.dto.signup.SignupResult;

@Service
public class SignupFacade {
	private MemberService memberService;
	private PasswordEncoder passwordEncoder;

	@Autowired
	public SignupFacade(MemberService memberService, PasswordEncoder passwordEncoder) {
		this.memberService = memberService;
		this.passwordEncoder = passwordEncoder;
	}

	public SignupResult signUp(SignupCommand command) {
		Member member = this.memberService.create(
			CreateCommand.builder()
				.email(command.getEmail())
				.encryptedPassword(this.passwordEncoder.encode(command.getPassword()))
				.build());
		return SignupResult.builder().email(member.getEmail()).signupDate(member.getCreatedAt()).build();
	}
}
