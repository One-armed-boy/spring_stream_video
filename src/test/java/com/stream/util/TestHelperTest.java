package com.stream.util;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import com.stream.domain.member.Member;
import com.stream.domain.member.MemberRepository;
import com.stream.domain.role.Role;
import com.stream.domain.role.RoleRepository;
import com.stream.domain.role.RolesEnum;
import com.stream.domain.video.Video;
import com.stream.domain.video.VideoRepository;

@Import(TestHelper.class)
@SpringBootTest
public class TestHelperTest {
	private final TestHelper testHelper;
	private final RoleRepository roleRepository;
	private final MemberRepository memberRepository;
	private final VideoRepository videoRepository;

	@Autowired
	public TestHelperTest(TestHelper testHelper, RoleRepository roleRepository, MemberRepository memberRepository,
		VideoRepository videoRepository) {
		this.testHelper = testHelper;
		this.roleRepository = roleRepository;
		this.memberRepository = memberRepository;
		this.videoRepository = videoRepository;
	}

	@Test
	@DisplayName("빈 DB > initTable 메서드 실행 > 초기 데이터 확인 성공")
	void initTable() {
		// given
		// when
		testHelper.initTables();

		// then
		List<Role> roles = roleRepository.findAll();

		Assertions.assertThat(roles.size()).isEqualTo(RolesEnum.values().length);
		List<String> expectedRolesNameList = Arrays.stream(RolesEnum.values())
			.map(rolesEnum -> rolesEnum.name())
			.toList();
		for (var role : roles) {
			Assertions.assertThat(role.getName().name())
				.isIn(expectedRolesNameList);
		}

		testHelper.clearTables();
	}

	@Test
	@DisplayName("모든 테이블 내 레코드 insert > clearTables 호출 > 빈 테이블")
	@Transactional
	void clearTables() {
		// given
		Role role = roleRepository.save(new Role(RolesEnum.ADMIN));
		Member member = memberRepository.save(
			Member.builder().email("test@test.com").password("12345qwerty").role(role).build());
		Video video = videoRepository.save(
			Video.builder().fileTag("test").extension("MOV").path("/test.MOV").size(0L).member(member).build());

		Assertions.assertThat(roleRepository.findAll().size()).isEqualTo(1);
		Assertions.assertThat(memberRepository.findAll().size()).isEqualTo(1);
		Assertions.assertThat(videoRepository.findAll().size()).isEqualTo(1);

		// when
		testHelper.clearTables();

		// then
		Assertions.assertThat(roleRepository.findAll()).isEmpty();
		Assertions.assertThat(memberRepository.findAll()).isEmpty();
		Assertions.assertThat(videoRepository.findAll()).isEmpty();
	}

	@Test
	@DisplayName("DB 초기 세팅 > 회원 가입 요청 > member 레코드 DB 내 존재")
	@Transactional
	void signup() {
		// given
		testHelper.initTables();
		var mockEmail = "test@test.com";
		var mockPwd = "1q2w3e4rasdf";

		// when
		Member member = testHelper.signup(mockEmail, mockPwd);

		// then
		Optional<Member> memberOpt = memberRepository.findMemberByEmail(mockEmail);
		Assertions.assertThat(memberOpt.isPresent()).isTrue();
		if (memberOpt.isPresent()) {
			Assertions.assertThat(memberOpt.get()).isEqualTo(member);
		}

		testHelper.clearTables();
	}
}
