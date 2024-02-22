package com.stream.domain.role.exception;

import com.stream.domain.role.RolesEnum;

public class RoleNotFoundException extends RuntimeException {
	public RoleNotFoundException(RolesEnum name) {
		super("Can't find role in DB (key: name, value: " + name + " )");
	}
}
