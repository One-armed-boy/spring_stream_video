package com.stream.domain.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stream.domain.member.dto.CreateCommand;
import com.stream.domain.member.exception.DuplicatedMemberCreateException;
import com.stream.domain.member.exception.MemberNotFoundException;

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
	public Member create(CreateCommand command) {
		String email = command.getEmail();
		if (this.isMemberExistByEmail(email)) {
			throw new DuplicatedMemberCreateException();
		}

		String password = command.getEncryptedPassword();
		return this.memberRepository.save(
			Member.builder().email(email).password(password).build());
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
