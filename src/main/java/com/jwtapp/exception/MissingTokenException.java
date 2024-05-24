package com.jwtapp.exception;

public class MissingTokenException extends RuntimeException {

	public MissingTokenException(String message, Throwable cause) {
		super(message, cause);
	}

	public MissingTokenException(String message) {
		super(message);
	}

}
