package com.jwtapp.response;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class ResponseHandler {
	
	public static ResponseEntity<?> responseBuilder(String message, HttpStatus httpStatus, Object responseObject) {
		Map<String, Object> responseBody = new HashMap<>();
		responseBody.put("message", message);
		responseBody.put("httpStatus", httpStatus);
		responseBody.put("data", responseObject);
		responseBody.put("timestamp", LocalDateTime.now());
		return ResponseEntity.status(httpStatus).body(responseBody);
	}

}
