package com.jwtapp.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jwtapp.dto.ChangePasswordDto;
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

	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationDto userRegistrationDto) {
		User registeredUser = userServiceImpl.registerUser(userRegistrationDto);
		return ResponseHandler.responseBuilder(
				"User " + registeredUser.getUserName() + " is successfully registered. PLEASE VERIFY YOUR EMAIL",
				HttpStatus.CREATED, registeredUser);
	}

	@GetMapping("/userlist")
	public ResponseEntity<?> getAllUser() {
		List<User> usrList = userServiceImpl.getAllUser();
		return ResponseHandler.responseBuilder("All registred user", HttpStatus.OK, usrList);
	}

	@GetMapping
	public ResponseEntity<?> getUserByEmail(@RequestHeader("Authorization") String authHeader) {
		User user = userServiceImpl.getUserByEmail(authHeader);
		return ResponseHandler.responseBuilder("Requested user", HttpStatus.OK, user);
	}

	@DeleteMapping
	public ResponseEntity<?> deleteUserByEmail(@RequestHeader("Authorization") String authHeader) {
		String email = userServiceImpl.deleteUserByEmail(authHeader);
		return ResponseHandler.responseBuilder("User with email " + email + " is successfully deleted.", HttpStatus.OK,
				null);
	}

	@PutMapping
	public ResponseEntity<?> updateUserByEmail(@Valid @RequestBody UserUpdateDto updateDto,
			@RequestHeader("Authorization") String authHeader) {
		User savedUser = userServiceImpl.updateUserByEmail(updateDto, authHeader);
		return ResponseHandler.responseBuilder("User " + savedUser.getUserName() + " is successfully updated.",
				HttpStatus.OK, savedUser);
	}

	@PatchMapping
	public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordDto changePasswordDto,
			@RequestHeader("Authorization") String authHeader) {
		userServiceImpl.changePassword(changePasswordDto, authHeader);
		return ResponseHandler.responseBuilder("Passowrd is successfully changed.", HttpStatus.OK, null);
	}

	@GetMapping("/noauth")
	public ResponseEntity<Object> permitMethod() {
		return new ResponseEntity<Object>("NO AUTHENTICATION REQUIRED", HttpStatus.OK);
	}

}
