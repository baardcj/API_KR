package com.bjc.web.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bjc.data.RoleRepository;
import com.bjc.model.Role;

@RestController
@RequestMapping(path = "/roles", produces = "application/json")
public class RoleController {
	
	private RoleRepository role_repo;
	
	public RoleController(RoleRepository role_repo) {
		this.role_repo = role_repo;
	}
	

	/*
	 * REQUIREMENT 4
	 * List all roles
	 */
	@GetMapping
	public Iterable<Role> getRoles() {
		return role_repo.findAll();
	}
}