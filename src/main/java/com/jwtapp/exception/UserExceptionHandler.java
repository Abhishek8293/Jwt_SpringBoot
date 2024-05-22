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

@ControllerAdvice
public class UserExceptionHandler {

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

}
