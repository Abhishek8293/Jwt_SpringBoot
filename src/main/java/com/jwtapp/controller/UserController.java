package com.jwtapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.jwtapp.dto.UserRegistrationDto;
import com.jwtapp.entity.User;
import com.jwtapp.response.ResponseHandler;
import com.jwtapp.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		super();
		this.userService = userService;
	}

	@PostMapping
	public ResponseEntity<Object> registerUser(@Valid @RequestBody UserRegistrationDto userDto) {
		User user = userService.registerUser(userDto);
		return ResponseHandler.responseBuilder("User " + user.getUserName() + " is registered successfully",
				HttpStatus.OK, user);
	}

	@GetMapping("/success")
	public ResponseEntity<Object> afterLoginSuccess() {
		return ResponseHandler.responseBuilder("Welcom after successfull login !!!", HttpStatus.OK, null);

	}

	@GetMapping("/noauth")
	public ResponseEntity<Object> permitMethod() {
		return ResponseHandler.responseBuilder("This API does not required authentication !!!", HttpStatus.OK, null);
	}

}