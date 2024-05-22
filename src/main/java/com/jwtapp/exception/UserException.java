package com.jwtapp.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserException {

	private final String message;
	private final Throwable throwable;
	private final HttpStatus httpStatus;

	public UserException(String message, Throwable throwable, HttpStatus httpStatus) {
		super();
		this.message = message;
		this.throwable = throwable;
		this.httpStatus = httpStatus;
	}

}
