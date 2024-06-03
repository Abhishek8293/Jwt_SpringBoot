package com.jwtapp.userexception;

public class UserAlreadyExistsException extends RuntimeException {

	public UserAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);	}

	public UserAlreadyExistsException(String message) {
		super(message);
	}
	

}
