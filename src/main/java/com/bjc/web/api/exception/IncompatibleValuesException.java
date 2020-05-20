package com.bjc.web.api.exception;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class IncompatibleValuesException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public IncompatibleValuesException(LocalDateTime date_1, LocalDate date_2) {
		super("incompatible from and to date/time,  '" + date_1.toString() + "', '" + date_2.toString() + "'");
	}
}
