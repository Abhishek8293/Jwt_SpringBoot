package com.jwtapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jwtapp.dto.ForgotPasswordDto;
import com.jwtapp.dto.LoginRequestDto;
import com.jwtapp.dto.ResetPasswordDto;
import com.jwtapp.response.ResponseHandler;
import com.jwtapp.service.AuthServiceImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

	private final AuthServiceImpl authServiceImpl;

	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDto loginRequest) {
		String jwtToken = authServiceImpl.login(loginRequest);
		return ResponseHandler.responseBuilder("Successfully logged in", HttpStatus.OK, jwtToken);
	}

	@GetMapping("/verify-email/{token}")
	public ResponseEntity<?> verifyUserByEmail(@PathVariable String token) {
		authServiceImpl.verifyUserByEmail(token);
		return ResponseHandler.responseBuilder("Email verified successfully.", HttpStatus.OK, null);
	}

	@GetMapping("/resend/{email}")
	public ResponseEntity<?> resendUserVerificationEmail(@PathVariable String email) {
		authServiceImpl.resendUserVerificationEmail(email);
		return ResponseHandler.responseBuilder("Verification mail is sent to : " + email, HttpStatus.OK, null);
	}

	@PostMapping("/forgot-password")
	public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordDto forgotPasswordDto) {
		authServiceImpl.forgotPassword(forgotPasswordDto);
		return ResponseHandler.responseBuilder("Reset password verification email sent successfully.", HttpStatus.OK,
				null);
	}

	@GetMapping("/verify/reset-password-email/{token}")
	public ResponseEntity<?> verifyResetPasswordEmail(@PathVariable String token) {
		authServiceImpl.verifyResetPasswordEmail(token);
		return ResponseHandler.responseBuilder("Reset Password Mail Verified Successfully.", HttpStatus.OK, null);
	}

	@PostMapping("/reset-password")
	public ResponseEntity<?> updatePassword(@Valid @RequestBody ResetPasswordDto resetPasswordDto) {
		authServiceImpl.updatePassword(resetPasswordDto);
		return ResponseHandler.responseBuilder("Your Password Is Successfully Updated.", HttpStatus.OK, null);
	}

}
