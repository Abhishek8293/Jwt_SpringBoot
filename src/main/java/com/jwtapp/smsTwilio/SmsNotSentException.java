package com.jwtapp.smsTwilio;

public class SmsNotSentException extends RuntimeException {

	public SmsNotSentException(String message, Throwable cause) {
		super(message, cause);
	}

	public SmsNotSentException(String message) {
		super(message);
	}

}
