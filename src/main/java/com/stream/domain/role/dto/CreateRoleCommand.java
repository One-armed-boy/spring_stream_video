package com.stream.domain.role.dto;

import com.stream.domain.role.RolesEnum;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CreateRoleCommand {
	private RolesEnum roleName;

	public CreateRoleCommand(RolesEnum rolesEnum) {
		this.roleName = rolesEnum;
	}
}
