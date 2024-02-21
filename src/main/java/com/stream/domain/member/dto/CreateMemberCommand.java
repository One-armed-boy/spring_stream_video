package com.stream.domain.member.dto;

import com.stream.domain.role.Role;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CreateMemberCommand {
	private String email;
	private String encryptedPassword;
	private Role role;
}
