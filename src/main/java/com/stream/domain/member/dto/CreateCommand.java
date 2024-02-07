package com.stream.domain.member.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CreateCommand {
	private String email;
	private String encryptedPassword;
}
