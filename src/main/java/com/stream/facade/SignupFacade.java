package com.stream.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stream.domain.member.Member;
import com.stream.domain.member.MemberService;
import com.stream.domain.member.dto.CreateMemberCommand;
import com.stream.domain.member.dto.signup.SignupCommand;
import com.stream.domain.member.dto.signup.SignupResult;
import com.stream.domain.role.RoleService;
import com.stream.domain.role.RolesEnum;

@Service
public class SignupFacade {
	private MemberService memberService;
	private RoleService roleService;
	private PasswordEncoder passwordEncoder;

	@Autowired
	public SignupFacade(MemberService memberService, RoleService roleService, PasswordEncoder passwordEncoder) {
		this.memberService = memberService;
		this.roleService = roleService;
		this.passwordEncoder = passwordEncoder;
	}

	@Transactional
	public SignupResult signUp(SignupCommand command) {
		Member member = memberService.create(
			CreateMemberCommand.builder()
				.email(command.getEmail())
				.encryptedPassword(passwordEncoder.encode(command.getPassword()))
				// TODO: 임시로 일괄 User 권한을 갖도록 작성한 것 수정
				.role(roleService.getRoleByName(RolesEnum.USER))
				.build());
		return SignupResult.builder().email(member.getEmail()).signupDate(member.getCreatedAt()).build();
	}
}
