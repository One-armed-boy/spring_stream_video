package com.stream.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.stream.domain.role.Role;
import com.stream.domain.role.RoleRepository;
import com.stream.domain.role.RolesEnum;

@TestComponent
public class TestHelper {
	private RoleRepository roleRepository;

	@Autowired
	public TestHelper(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
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
	public void clearTables(JpaRepository... repositories) {
		// role 테이블에는 initDB() 내에서 명시적으로 생성한 데이터가 존재하기 때문에 항상 비워줌
		roleRepository.deleteAll();

		for (var repository : repositories) {
			repository.deleteAll();
		}
	}
}
