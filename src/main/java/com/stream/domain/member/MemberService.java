package com.stream.domain.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stream.domain.member.dto.CreateMemberCommand;
import com.stream.domain.member.exception.DuplicatedMemberCreateException;
import com.stream.domain.member.exception.MemberNotFoundException;
import com.stream.domain.role.Role;

@Service
@Transactional(readOnly = true)
public class MemberService {
	private MemberRepository memberRepository;

	@Autowired
	public MemberService(MemberRepository memberRepository) {
		this.memberRepository = memberRepository;
	}

	public Member getMemberByEmail(String email) {
		return this.memberRepository.findMemberByEmail(email)
			.orElseThrow(() -> new MemberNotFoundException(email));
	}

	@Transactional
	public Member create(CreateMemberCommand command) {
		String email = command.getEmail();
		if (this.isMemberExistByEmail(email)) {
			throw new DuplicatedMemberCreateException();
		}

		String password = command.getEncryptedPassword();
		Role role = command.getRole();
		return this.memberRepository.save(
			Member.builder().email(email).password(password).role(role).build());
	}

	private boolean isMemberExistByEmail(String email) {
		try {
			this.getMemberByEmail(email);
			return true;
		} catch (MemberNotFoundException exception) {
			return false;
		}
	}
}
