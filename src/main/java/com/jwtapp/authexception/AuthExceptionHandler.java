package com.jwtapp.authexception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AuthExceptionHandler {

	@ExceptionHandler(value = { BadCredentialsException.class })
	public ResponseEntity<?> handleBadCredentialsException(BadCredentialsException ex) {
		Map<String, Object> responseBody = new HashMap<>();
		responseBody.put("error", ex.getMessage());
		responseBody.put("httpStatus", HttpStatus.UNAUTHORIZED);
		responseBody.put("timestamp", LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
	}

	@ExceptionHandler(value = { DisabledException.class })
	public ResponseEntity<?> handleDisabledException(DisabledException ex) {
		Map<String, Object> responseBody = new HashMap<>();
		responseBody.put("error", ex.getMessage());
		responseBody.put("httpStatus", HttpStatus.UNAUTHORIZED);
		responseBody.put("timestamp", LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
	}

	@ExceptionHandler(value = { verificationTokenExpiredException.class })
	public ResponseEntity<Object> handleVerificationTokenExpiredException(verificationTokenExpiredException ex) {
		Map<String, Object> responseBody = new HashMap<>();
		responseBody.put("error", ex.getMessage());
		responseBody.put("httpStatus", HttpStatus.UNAUTHORIZED);
		responseBody.put("timestamp", LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
	}

}
