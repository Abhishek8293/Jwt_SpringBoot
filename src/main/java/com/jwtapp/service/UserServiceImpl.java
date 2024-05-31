package com.jwtapp.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jwtapp.dto.UserRegistrationDto;
import com.jwtapp.dto.UserUpdateDto;
import com.jwtapp.entity.User;
import com.jwtapp.entity.VerificationToken;
import com.jwtapp.mail.MailServiceImpl;
import com.jwtapp.repository.UserRepository;
import com.jwtapp.repository.VerificationTokenRepository;
import com.jwtapp.userexception.UserNotFoundException;

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
		User newUser = User.builder().userName(userRegistrationDto.getUserName()).email(userRegistrationDto.getEmail())
				.password(passwordEncoder.encode(userRegistrationDto.getPassword()))
				.roles(userRegistrationDto.getRoles()).isActive(false).build();
		User savedUser = userRepository.save(newUser);

		// Generate Verification Token
		String token = UUID.randomUUID().toString();

		// Save Token
		VerificationToken verificationToken = VerificationToken.builder().token(token).expiryDate(LocalDateTime.now())
				.user(newUser).build();
		verificationTokenRepository.save(verificationToken);

		// Send Mail
		mailServiceImpl.sendVerificationMail(savedUser, token);

		return savedUser;
	}

	@Override
	public List<User> getAllUser() {
		List<User> userList = userRepository.findAll();
		return userList;
	}

	@Override
	public User getUserByEmail(String email) {
		Optional<User> user = userRepository.findByEmail(email);
		if (user.isEmpty()) {
			throw new UserNotFoundException("Requested user does not exist.");
		}
		return user.get();
	}

	@Override
	public void deleteUserByEmail(String email) {
		Optional<User> user = userRepository.findByEmail(email);
		if (user.isEmpty()) {
			throw new UserNotFoundException("Requested user does not exist.");
		}
		userRepository.deleteById(user.get().getUserId());
	}

	@Override
	public User updateUserByEmail(UserUpdateDto userUpdateDto, String email) {
		Optional<User> user = userRepository.findByEmail(email);
		if (user.isEmpty()) {
			throw new UserNotFoundException("Requested user does not exist.");
		}
		User existingUser = user.get();
		existingUser.builder()
		.userName(userUpdateDto.getUserName())
		.email(userUpdateDto.getEmail())
		.password(userUpdateDto.getPassword())
		.build();
		User savedUser = userRepository.save(existingUser);
		return savedUser;
	}

}
