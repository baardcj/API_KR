package com.bjc.web.api.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class IncompletePayloadException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public IncompletePayloadException(String field, String value) {
		super("Payload must include field: '" + field + "' assigned with an value of type: " + value);
	}
}
