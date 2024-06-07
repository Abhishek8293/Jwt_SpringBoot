package com.jwtapp.smsTwilio;

public class OtpExpiredException extends RuntimeException {

	public OtpExpiredException(String message, Throwable cause) {
		super(message, cause);
	}

	public OtpExpiredException(String message) {
		super(message);
	}

}
