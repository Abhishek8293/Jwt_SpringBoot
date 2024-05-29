package com.jwtapp.exception;

public class MissingJwtTokenException extends RuntimeException {

	public MissingJwtTokenException(String message, Throwable cause) {
		super(message, cause);
	}

	public MissingJwtTokenException(String message) {
		super(message);
	}
	
	

}
