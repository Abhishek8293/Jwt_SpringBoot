package com.jwtapp.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.mail.MessagingException;

@ControllerAdvice
public class MailExceptionHandler {
	
	@ExceptionHandler(value = {MessagingException.class})
	public ResponseEntity<Object> handleMessagingException(MessagingException messagingException){
		Map<String, Object> response =  new HashMap<>();
		response.put("message", messagingException.getMessage());
		response.put("throwable", messagingException.getCause());
		response.put("httpStatus", HttpStatus.BAD_REQUEST);
		return new ResponseEntity<Object>(response,HttpStatus.BAD_REQUEST);
	}

}
