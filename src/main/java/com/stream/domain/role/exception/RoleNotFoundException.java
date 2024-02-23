package com.stream.domain.role.exception;

import com.stream.domain.role.RolesEnum;
import com.stream.exception.BaseException;
import com.stream.exception.code.RoleExceptionCode;

public class RoleNotFoundException extends BaseException {
	public RoleNotFoundException(RolesEnum name) {
		super(RoleExceptionCode.ROLE_NOT_FOUND, "Can't find role in DB (key: name, value: " + name + " )");
	}
}
