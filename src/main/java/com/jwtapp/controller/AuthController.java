package com.jwtapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jwtapp.dto.LoginRequest;
import com.jwtapp.response.ResponseHandler;
import com.jwtapp.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

	private final AuthService authService;

	@Transactional
	@GetMapping("/verify/{token}")
	public ResponseEntity<Object> verifyUser(@PathVariable String token) {
		authService.verifyUser(token);
		return ResponseHandler.responseBuilder("Email verified successfully.", HttpStatus.OK, null);
	}

	@PostMapping("/login")
	public ResponseEntity<Object> login(@Valid @RequestBody LoginRequest loginRequest) {
		String jwtToken = authService.login(loginRequest);
		return ResponseHandler.responseBuilder("Successfully logged in", HttpStatus.OK, jwtToken);
	}

	@GetMapping("/resend/{email}")
	public ResponseEntity<Object> resendVerificationEmail() {
		authService.resendVerificationEmail();
		return ResponseHandler.responseBuilder(null, null, null);
	}

}
