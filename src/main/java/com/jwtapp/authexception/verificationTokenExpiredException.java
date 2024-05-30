package com.jwtapp.authexception;

public class verificationTokenExpiredException extends RuntimeException  {

	public verificationTokenExpiredException(String message, Throwable cause) {
		super(message, cause);
	}

	public verificationTokenExpiredException(String message) {
		super(message);
	}

}
