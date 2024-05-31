package com.jwtapp.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jwtapp.dto.UserRegistrationDto;
import com.jwtapp.dto.UserUpdateDto;
import com.jwtapp.entity.User;
import com.jwtapp.response.ResponseHandler;
import com.jwtapp.service.UserServiceImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

	private final UserServiceImpl userServiceImpl;

	@PostMapping
	public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationDto userRegistrationDto) {
		User registeredUser = userServiceImpl.registerUser(userRegistrationDto);
		return ResponseHandler.responseBuilder("User " + registeredUser.getUserName() + " is successfully registered. PLEASE VERIFY YOUR EMAIL",
				HttpStatus.CREATED, registeredUser);
	}

	@GetMapping
	public ResponseEntity<?> getAllUser() {
		List<User> usrList = userServiceImpl.getAllUser();
		return ResponseHandler.responseBuilder("All registred user", HttpStatus.OK, usrList);
	}

	@GetMapping("{email}")
	public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
		User user = userServiceImpl.getUserByEmail(email);
		return ResponseHandler.responseBuilder("Requested user", HttpStatus.OK, user);
	}

	@DeleteMapping("{email}")
	public ResponseEntity<?> deleteUserByEmail(@PathVariable String email) {
		userServiceImpl.deleteUserByEmail(email);
		return ResponseHandler.responseBuilder("User with email " + email + " is successfully deleted.", HttpStatus.OK,
				null);
	}

	@PutMapping("{email}")
	public ResponseEntity<?> updateUserByEmail(@Valid @RequestBody UserUpdateDto updateDto, @PathVariable String email) {
		User savedUser = userServiceImpl.updateUserByEmail(updateDto, email);
		return ResponseHandler.responseBuilder("User " + savedUser.getUserName() + " is successfully updated.",
				HttpStatus.OK, savedUser);
	}

	@GetMapping("/noauth")
	public ResponseEntity<Object> permitMethod() {
		return new ResponseEntity<Object>("NO AUTHENTICATION REQUIRED", HttpStatus.OK);
	}

}
