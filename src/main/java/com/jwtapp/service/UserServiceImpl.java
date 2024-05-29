package com.jwtapp.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jwtapp.dto.UserRegistrationDto;
import com.jwtapp.mail.MailServiceImpl;
import com.jwtapp.user.User;
import com.jwtapp.user.UserRepository;
import com.jwtapp.verificationtoken.VerificationToken;
import com.jwtapp.verificationtoken.VerificationTokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	private final VerificationTokenRepository verificationTokenRepository;

	private final PasswordEncoder passwordEncoder;

	private final MailServiceImpl mailServiceImpl;

	@Override
	public User registerUser(UserRegistrationDto userRegistrationDto) {

		// Save User with inactive status
		User newUser = User.builder()
				.userName(userRegistrationDto.getUserName())
				.email(userRegistrationDto.getEmail())
				.password(passwordEncoder.encode(userRegistrationDto.getPassword()))
				.roles(userRegistrationDto.getRoles())
				.isActive(false)
				.build();
		User savedUser = userRepository.save(newUser);
	
		// Generate Verification Token
		String token = UUID.randomUUID().toString();

		// Save Token
		VerificationToken verificationToken = VerificationToken.builder()
				.token(token)
				.expiryDate(LocalDateTime.now())
				.user(newUser).build();
		verificationTokenRepository.save(verificationToken);

		// Send Mail
		mailServiceImpl.sendVerificationMail(savedUser, token);

		return savedUser;
	}

}
