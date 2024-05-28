package com.jwtapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

	@GetMapping
	public ResponseEntity<Object> afterLoginSuccessUser() {
		return new ResponseEntity<Object>("Welcome to home page --- SUCCESSFULL AUTHENTICATION ADMIN---", HttpStatus.OK);
	}

}
