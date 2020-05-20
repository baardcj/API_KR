package com.bjc.web.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class VersionMisMatchException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public VersionMisMatchException(Long paramVersion, Long userId) {
		super("version '" + paramVersion + "' does not match current version of entity '" + userId + "'");
	}
}