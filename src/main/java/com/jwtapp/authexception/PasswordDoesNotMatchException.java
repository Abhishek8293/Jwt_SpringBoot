package com.jwtapp.authexception;

public class PasswordDoesNotMatchException extends RuntimeException {

	public PasswordDoesNotMatchException(String message, Throwable cause) {
		super(message, cause);
	}

	public PasswordDoesNotMatchException(String message) {
		super(message);
	}

}
