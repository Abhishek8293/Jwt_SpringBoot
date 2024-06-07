package com.jwtapp.smsTwilio;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class SmsExceptionHandler {

	@ExceptionHandler(value = { SmsNotSentException.class })
	public ResponseEntity<?> handleSmsNotSentException(SmsNotSentException ex) {
		Map<String, Object> responseBody = new HashMap<>();
		responseBody.put("error", ex.getMessage());
		responseBody.put("httpStatus", HttpStatus.BAD_REQUEST);
		responseBody.put("timestamp", LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
	}

	@ExceptionHandler(value = { OtpNotFoundException.class })
	public ResponseEntity<?> handleOtpNotFoundException(OtpNotFoundException ex) {
		Map<String, Object> responseBody = new HashMap<>();
		responseBody.put("error", ex.getMessage());
		responseBody.put("httpStatus", HttpStatus.NOT_FOUND);
		responseBody.put("timestamp", LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
	}

	@ExceptionHandler(value = { OtpExpiredException.class })
	public ResponseEntity<?> handleOtpExpiredException(OtpExpiredException ex) {
		Map<String, Object> responseBody = new HashMap<>();
		responseBody.put("error", ex.getMessage());
		responseBody.put("httpStatus", HttpStatus.BAD_REQUEST);
		responseBody.put("timestamp", LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
	}

}
