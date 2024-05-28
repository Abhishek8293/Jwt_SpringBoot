package com.jwtapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jwtapp.dto.UserRegistrationDto;
import com.jwtapp.service.UserServiceImpl;
import com.jwtapp.user.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
	
	private final UserServiceImpl userServiceImpl;
	
	@PostMapping
	public ResponseEntity<User> registerUser(@RequestBody UserRegistrationDto userRegistrationDto){
		User registeredUser = userServiceImpl.registerUser(userRegistrationDto);
		return new ResponseEntity<>(registeredUser,HttpStatus.CREATED);
	}
	
	@GetMapping
	public ResponseEntity<Object> permitMethod() {
		return new ResponseEntity<Object>("NO AUTHENTICATION REQUIRED", HttpStatus.OK);
	}
	

}
