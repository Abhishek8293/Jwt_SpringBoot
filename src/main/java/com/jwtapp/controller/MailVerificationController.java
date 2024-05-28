package com.jwtapp.controller;

import java.time.LocalDateTime;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.jwtapp.user.User;
import com.jwtapp.user.UserRepository;
import com.jwtapp.verificationtoken.VerificationToken;
import com.jwtapp.verificationtoken.VerificationTokenRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MailVerificationController {

	private final VerificationTokenRepository verificationTokenRepository;

	private final UserRepository userRepository;

	@Transactional
	@GetMapping("/verify/{token}")
	public String verifyUser(@PathVariable String token) {

		System.out.println("-------> INSIDE THE VERIFY USER REST API <--------");
		VerificationToken verificationToken = verificationTokenRepository.findByToken(token);

		LocalDateTime now = LocalDateTime.now();
		LocalDateTime expiryWithBuffer = verificationToken.getExpiryDate().plusSeconds(60);

		if (verificationToken == null || now.isAfter(expiryWithBuffer)) {
			return "Invalid or expired verification token.";
		}

		User user = verificationToken.getUser();
		user.setEnabled(true);
		userRepository.save(user);

		return "Email verified successfully.";
	}

}
