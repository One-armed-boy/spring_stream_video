package com.stream.domain.role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stream.domain.role.dto.CreateRoleCommand;
import com.stream.domain.role.exception.RoleNotFoundException;

@Service
@Transactional(readOnly = true)
public class RoleService {
	private RoleRepository roleRepository;

	@Autowired
	public RoleService(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	public Role getRoleByName(RolesEnum name) {
		return roleRepository.findRoleByName(name).orElseThrow(() -> new RoleNotFoundException(name));
	}

	@Transactional
	public Role create(CreateRoleCommand command) {
		var name = command.getRoleName();
		Role role = new Role(name);
		return roleRepository.save(role);
	}
}
