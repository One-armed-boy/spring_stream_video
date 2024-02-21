package com.stream.domain.role.dto;

import com.stream.domain.role.RolesEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateRoleCommand {
	private RolesEnum roleName;
}
