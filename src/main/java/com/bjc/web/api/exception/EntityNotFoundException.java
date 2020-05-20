package com.bjc.web.api.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public EntityNotFoundException(String entityType, Long id) {
		super(entityType + " with id: " + id.toString() + " not found");
	}

	public EntityNotFoundException(String entityType, String id) {
		super(entityType + " with id: " + id + " not found");
	}

}