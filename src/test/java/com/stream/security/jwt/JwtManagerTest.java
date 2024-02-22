package com.stream.security.jwt;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.stream.domain.role.Role;
import com.stream.domain.role.RolesEnum;

@SpringBootTest
public class JwtManagerTest {
	private JwtManager jwtManager;

	@Autowired
	public JwtManagerTest(JwtManager jwtManager) {
		this.jwtManager = jwtManager;
	}

	@Test
	@DisplayName("email, role을 이용하여 DecodedToken 구성 > 토큰 인코딩 후 다시 디코딩 > 결과 동일")
	void tokenTest() {
		// given
		var email = "test@test.com";
		var role = new Role(RolesEnum.ADMIN);
		var decodedToken = new DecodedJwtAccessToken(email, role);

		// when
		var token = jwtManager.issueToken(decodedToken);
		var reDecodeToken = jwtManager.decodeToken(token);

		// then
		Assertions.assertThat(decodedToken.getEmail()).isEqualTo(reDecodeToken.getEmail());
		Assertions.assertThat(decodedToken.getRole().getName()).isEqualTo(reDecodeToken.getRole().getName());
	}

	// TODO: 시간 모킹을 통한 토큰 만료 테스트 필요
}
