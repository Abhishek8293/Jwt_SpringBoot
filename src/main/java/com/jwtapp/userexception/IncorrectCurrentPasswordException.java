package com.jwtapp.userexception;

public class IncorrectCurrentPasswordException  extends RuntimeException{

	public IncorrectCurrentPasswordException(String message, Throwable cause) {
		super(message, cause);
	}

	public IncorrectCurrentPasswordException(String message) {
		super(message);
	}
	
}
