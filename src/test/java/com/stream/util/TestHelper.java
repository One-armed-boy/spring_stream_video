package com.stream.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.transaction.annotation.Transactional;

import com.stream.domain.member.MemberRepository;
import com.stream.domain.role.Role;
import com.stream.domain.role.RoleRepository;
import com.stream.domain.role.RolesEnum;
import com.stream.domain.video.VideoRepository;

@TestComponent
public class TestHelper {
	private final RoleRepository roleRepository;
	private final MemberRepository memberRepository;
	private final VideoRepository videoRepository;

	@Autowired
	public TestHelper(RoleRepository roleRepository, MemberRepository memberRepository,
		VideoRepository videoRepository) {
		this.roleRepository = roleRepository;
		this.memberRepository = memberRepository;
		this.videoRepository = videoRepository;
	}

	@Transactional
	public void initDB() {
		initRole();
	}

	private void initRole() {
		// Role 정보는 테이블 내 항상 존재해야하기 때문에 명시적으로 생성
		// ref: src/main/resources/db/migration/V1.1__init_role.sql
		for (var role : RolesEnum.values()) {
			roleRepository.save(new Role(role));
		}
	}

	@Transactional
	public void clearTables() {
		roleRepository.deleteAll();
		memberRepository.deleteAll();
		videoRepository.deleteAll();
	}
}
