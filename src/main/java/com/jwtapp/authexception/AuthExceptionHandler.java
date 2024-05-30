package com.jwtapp.authexception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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
	
	@ExceptionHandler(value = { MethodArgumentNotValidException.class })
	public ResponseEntity<Object> handleMethodArgumentNotValidException(
			MethodArgumentNotValidException methodArgumentNotValidException) {
		Map<String, Object> responseMap = new HashMap<>();
		List<ObjectError> erroList = methodArgumentNotValidException.getBindingResult().getAllErrors();
		for (ObjectError error : erroList) {
			String fieldName = ((FieldError) error).getField();
			String messageString = error.getDefaultMessage();
			responseMap.put(fieldName, messageString);
		}
		responseMap.put("httpStatus", HttpStatus.BAD_REQUEST);

		return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
	}
	

}
