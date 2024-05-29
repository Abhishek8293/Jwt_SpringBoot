package com.jwtapp.userexception;

import org.springframework.http.HttpStatus;

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
