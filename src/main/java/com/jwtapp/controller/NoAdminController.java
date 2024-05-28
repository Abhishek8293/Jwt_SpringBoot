package com.jwtapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/noadmin")
public class NoAdminController {
	
	@GetMapping
	public ResponseEntity<Object> afterLoginSuccessAdmin() {
		return new ResponseEntity<Object>("Welcome to home page --- SUCCESSFULL AUTHENTICATION USER---", HttpStatus.OK);
	}

}
