package com.jwtapp.userexception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserExceptionHandler {

	@ExceptionHandler(value = { MethodArgumentNotValidException.class })
	public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		Map<String, Object> responseBody = new HashMap<>();
		List<ObjectError> erroList = ex.getBindingResult().getAllErrors();
		for (ObjectError error : erroList) {
			String fieldName = ((FieldError) error).getField();
			String messageString = error.getDefaultMessage();
			responseBody.put(fieldName, messageString);
		}
		responseBody.put("timestamp", LocalDateTime.now());
		responseBody.put("httpStatus", HttpStatus.BAD_REQUEST);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException ex) {
		Map<String, Object> responseBody = new HashMap<>();
		responseBody.put("error", ex.getMessage());
		responseBody.put("httpStatus", HttpStatus.NOT_FOUND);
		responseBody.put("timestamp", LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
	}
	
	@ExceptionHandler(value = {IncorrectCurrentPasswordException.class})
	public ResponseEntity<?> handleIncorrectCurrentPasswordException(IncorrectCurrentPasswordException ex){
		Map<String, Object> responseBody = new HashMap<>();
		responseBody.put("error", ex.getMessage());
		responseBody.put("httpStatus", HttpStatus.BAD_REQUEST);
		responseBody.put("timestamp", LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
	}
	
	@ExceptionHandler(value = {UserAlreadyExistsException.class})
	public ResponseEntity<?> handleUserAlreadyExistsException(UserAlreadyExistsException ex){
		Map<String, Object> responseBody = new HashMap<>();
		responseBody.put("error", ex.getMessage());
		responseBody.put("httpStatus", HttpStatus.CONFLICT);
		responseBody.put("timestamp", LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.CONFLICT).body(responseBody);
	}
}
