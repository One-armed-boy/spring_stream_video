package com.stream.domain.member.dto;

import com.stream.domain.role.Role;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CreateMemberCommand {
	private String email;
	private String encryptedPassword;
	private Role role;

	@Builder
	public CreateMemberCommand(String email, String encryptedPassword, Role role) {
		this.email = email;
		this.encryptedPassword = encryptedPassword;
		this.role = role;
	}
}
