package com.stream.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.transaction.annotation.Transactional;

import com.stream.domain.member.Member;
import com.stream.domain.member.MemberRepository;
import com.stream.domain.member.MemberService;
import com.stream.domain.member.dto.signup.SignupCommand;
import com.stream.domain.role.Role;
import com.stream.domain.role.RoleRepository;
import com.stream.domain.role.RolesEnum;
import com.stream.domain.video.VideoRepository;
import com.stream.facade.SignupFacade;

@TestComponent
@Transactional
public class TestHelper {
	private final RoleRepository roleRepository;
	private final MemberRepository memberRepository;
	private final VideoRepository videoRepository;
	private final SignupFacade signupFacade;
	private final MemberService memberService;

	@Autowired
	public TestHelper(RoleRepository roleRepository, MemberRepository memberRepository,
		VideoRepository videoRepository, SignupFacade signupFacade, MemberService memberService) {
		this.roleRepository = roleRepository;
		this.memberRepository = memberRepository;
		this.videoRepository = videoRepository;
		this.signupFacade = signupFacade;
		this.memberService = memberService;
	}

	public void initTables() {
		initRole();
	}

	private void initRole() {
		// Role 정보는 테이블 내 항상 존재해야하기 때문에 명시적으로 생성
		// ref: src/main/resources/db/migration/V1.1__init_role.sql
		for (var role : RolesEnum.values()) {
			roleRepository.save(new Role(role));
		}
	}

	public void clearTables() {
		videoRepository.deleteAll();
		memberRepository.deleteAll();
		roleRepository.deleteAll();
	}

	public Member signup(String email, String password) {
		signupFacade.signUp(SignupCommand.builder().email(email).password(password).build());
		return memberService.getMemberByEmail(email);
	}
}
