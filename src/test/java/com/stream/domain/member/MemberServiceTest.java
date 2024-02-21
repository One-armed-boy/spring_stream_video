package com.stream.domain.member;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.stream.domain.member.dto.CreateMemberCommand;
import com.stream.domain.member.exception.DuplicatedMemberCreateException;
import com.stream.domain.member.exception.MemberNotFoundException;
import com.stream.domain.role.Role;
import com.stream.domain.role.RoleRepository;
import com.stream.domain.role.RoleService;
import com.stream.domain.role.RolesEnum;
import com.stream.domain.role.dto.CreateRoleCommand;

@SpringBootTest
public class MemberServiceTest {
	private MemberService memberService;
	private MemberRepository memberRepository;
	private RoleService roleService;
	private RoleRepository roleRepository;
	private PasswordEncoder passwordEncoder;

	@Autowired
	public MemberServiceTest(MemberService memberService, MemberRepository memberRepository, RoleService roleService,
		RoleRepository roleRepository,
		PasswordEncoder passwordEncoder) {
		this.memberService = memberService;
		this.memberRepository = memberRepository;
		this.roleService = roleService;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@BeforeEach
	void initRole() {
		roleService.create(new CreateRoleCommand(RolesEnum.USER));
		roleService.create(new CreateRoleCommand(RolesEnum.UPLOADER));
		roleService.create(new CreateRoleCommand(RolesEnum.ADMIN));
	}

	@AfterEach
	void cleanDB() {
		this.memberRepository.deleteAll();
		this.roleRepository.deleteAll();
	}

	@Test
	@DisplayName("빈 DB > 멤버 생성 > 정상 생성")
	void createMember() {
		// given
		String mockEmail = "test@test.com";
		String mockPwd = passwordEncoder.encode("abcdefgh");
		Role role = roleService.getRoleByName(RolesEnum.USER);
		CreateMemberCommand createCommand = CreateMemberCommand.builder()
			.email(mockEmail)
			.encryptedPassword(mockPwd)
			.role(role)
			.build();

		// when
		this.memberService.create(createCommand);

		// then
		List<Member> members = this.memberRepository.findAll();
		Assertions.assertThat(members.size()).isEqualTo(1);

		Member member = members.get(0);
		Assertions.assertThat(member.getEmail()).isEqualTo(mockEmail);
		Assertions.assertThat(member.getPassword()).isEqualTo(mockPwd);
	}

	@Test
	@DisplayName("DB 내 특정 메일을 갖는 멤버 존재 > 동일한 메일을 갖는 멤버 생성 요청 > 예외 발생")
	void createDuplicatedMember() {
		// given
		String mockEmail = "abc@test.com";
		String mockPwd = this.passwordEncoder.encode("abcdefg");
		Role role = roleService.getRoleByName(RolesEnum.USER);
		this.memberService.create(CreateMemberCommand.builder().email(mockEmail).encryptedPassword(mockPwd).build());
		CreateMemberCommand commandForCreateDuplicatedEmail = CreateMemberCommand.builder()
			.email(mockEmail)
			.encryptedPassword(mockPwd)
			.role(role)
			.build();

		Assertions.assertThatThrownBy(() -> {
			// when
			this.memberService.create(commandForCreateDuplicatedEmail);
			// then
		}).isInstanceOf(DuplicatedMemberCreateException.class);
	}

	@Test
	@DisplayName("DB 내 특정 메일을 갖는 멤버 존재 > 다른 메일을 갖는 멤버 생성 요청 > 정상 생성")
	void createDifferentMember() {
		// given
		String mockEmailInDB = "abc@test.com";
		String mockPwd = this.passwordEncoder.encode("abcdefg");
		Role role = roleService.getRoleByName(RolesEnum.USER);
		this.memberService.create(
			CreateMemberCommand.builder().email(mockEmailInDB).encryptedPassword(mockPwd).role(role).build());
		List<Member> membersBeforeTest = this.memberRepository.findAll();
		Assertions.assertThat(membersBeforeTest.size()).isEqualTo(1);

		String mockEmailNotInDB = "def@test.com";
		CreateMemberCommand command = CreateMemberCommand.builder()
			.email(mockEmailNotInDB)
			.encryptedPassword(mockPwd)
			.role(role)
			.build();

		// when
		this.memberService.create(command);

		// then
		List<Member> membersAfterTest = this.memberRepository.findAll();
		Assertions.assertThat(membersAfterTest.size()).isEqualTo(2);
	}

	@Test
	@DisplayName("특정 이메일의 멤버 DB 내 존재 > 다른 이메일을 이용하여 조회 > 예외 발생")
	void getMemberByEmailButNotFound() {
		// given
		String mockEmailInDB = "abc@test.com";
		String mockPwd = this.passwordEncoder.encode("abcdefg");
		Role role = roleService.getRoleByName(RolesEnum.USER);
		this.memberService.create(
			CreateMemberCommand.builder().email(mockEmailInDB).encryptedPassword(mockPwd).role(role).build());

		String mockEmailForSearch = "def@test.com";

		Assertions.assertThatThrownBy(() -> {
			// when
			this.memberService.getMemberByEmail(mockEmailForSearch);
			// then
		}).isInstanceOf(MemberNotFoundException.class);
	}

	@Test
	@DisplayName("특정 이메일의 멤버 DB 내 존재 > 해당 이메일을 이용하여 조회 > 조회 성공")
	void getMemberByEmail() {
		// given
		String mockEmail = "abc@test.com";
		String mockPwd = this.passwordEncoder.encode("abcdefg");
		Role role = roleService.getRoleByName(RolesEnum.USER);
		Member memberToSave = this.memberService.create(
			CreateMemberCommand.builder().email(mockEmail).encryptedPassword(mockPwd).role(role).build());

		// when
		Member memberInDB = this.memberService.getMemberByEmail(mockEmail);

		// then
		Assertions.assertThat(memberToSave.getId()).isEqualTo(memberInDB.getId());
		Assertions.assertThat(memberToSave.getEmail()).isEqualTo(memberInDB.getEmail());
		Assertions.assertThat(memberToSave.getPassword()).isEqualTo(memberInDB.getPassword());
		Assertions.assertThat(memberToSave.getCreatedAt()).isEqualTo(memberInDB.getCreatedAt());
	}
}
