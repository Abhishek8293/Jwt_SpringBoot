package com.jwtapp.authexception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import io.jsonwebtoken.MalformedJwtException;

@ControllerAdvice
public class AuthExceptionHandler {

	@ExceptionHandler(value = { BadCredentialsException.class })
	public ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException badCredentialsException) {
		AuthException authException = new AuthException(badCredentialsException.getMessage(),
				badCredentialsException.getCause(), HttpStatus.UNAUTHORIZED);
		return new ResponseEntity<>(authException, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(value = { DisabledException.class })
	public ResponseEntity<Object> handleDisabledException(DisabledException disabledException) {
		AuthException authException = new AuthException(disabledException.getMessage(), disabledException.getCause(),
				HttpStatus.UNAUTHORIZED);
		return new ResponseEntity<>(authException, HttpStatus.UNAUTHORIZED);
	}
	
//	@ExceptionHandler(value = { AccessDeniedException.class })
//	public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException accessDeniedException) {
//		AuthException authException = new AuthException(accessDeniedException.getMessage(), accessDeniedException.getCause(),
//				HttpStatus.FORBIDDEN);
//		return new ResponseEntity<>(authException, HttpStatus.UNAUTHORIZED);
//	}

}
