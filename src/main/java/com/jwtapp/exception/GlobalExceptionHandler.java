package com.jwtapp.exception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import io.jsonwebtoken.ExpiredJwtException;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> methodArgumentNotValidExceptionHandler(
			MethodArgumentNotValidException methodArgumentNotValidException) {
		Map<String, String> response = new HashMap<>();
		List<ObjectError> errors = methodArgumentNotValidException.getBindingResult().getAllErrors();
		for (ObjectError error : errors) {
			String fieldName = ((FieldError) error).getField();
			String message = error.getDefaultMessage();
			response.put(fieldName, message);
		}

		return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<Object> userNotFoundExceptionHandler(UserNotFoundException userNotFoundException) {

		UserException userException = new UserException(userNotFoundException.getMessage(),
				userNotFoundException.getCause(), HttpStatus.NOT_FOUND);
		return new ResponseEntity<Object>(userException, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(ExpiredJwtException.class)
	public ResponseEntity<Object> expiredJwtExceptionHandler(ExpiredJwtException expiredJwtException) {
		return new ResponseEntity<Object>(expiredJwtException.getMessage(), HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(TokenNotFoundException.class)
	public ResponseEntity<Object> tokenNotFoundException(TokenNotFoundException tokenNotFoundException) {
		TokenException tokenException = new TokenException(tokenNotFoundException.getMessage(),
				tokenNotFoundException.getCause(), HttpStatus.NOT_FOUND);
		return new ResponseEntity<Object>(tokenException, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(UserAlreadyExistsException.class)
	public ResponseEntity<Object> userAlreadyExistsExceptionHandler(
			UserAlreadyExistsException userAlreadyExistsException) {
		UserException userException = new UserException(userAlreadyExistsException.getMessage(),
				userAlreadyExistsException.getCause(), HttpStatus.CONFLICT);
		return new ResponseEntity<Object>(userException, HttpStatus.CONFLICT);
	}
}
