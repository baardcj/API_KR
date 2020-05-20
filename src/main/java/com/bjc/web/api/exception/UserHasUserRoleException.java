package com.bjc.web.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class UserHasUserRoleException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UserHasUserRoleException(Long userId) {
		super("User '" + userId + "' has user role / overlapping user role");
	}
}