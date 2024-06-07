package com.jwtapp.smsTwilio;

public class OtpNotFoundException extends RuntimeException {

	public OtpNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public OtpNotFoundException(String message) {
		super(message);
	}

}
