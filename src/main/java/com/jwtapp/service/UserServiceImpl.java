package com.jwtapp.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jwtapp.dto.ChangePasswordDto;
import com.jwtapp.dto.UserRegistrationDto;
import com.jwtapp.dto.UserUpdateDto;
import com.jwtapp.entity.User;
import com.jwtapp.entity.VerificationToken;
import com.jwtapp.mail.MailServiceImpl;
import com.jwtapp.repository.UserRepository;
import com.jwtapp.repository.VerificationTokenRepository;
import com.jwtapp.securityconfig.JwtService;
import com.jwtapp.userexception.IncorrectCurrentPasswordException;
import com.jwtapp.userexception.UserAlreadyExistsException;
import com.jwtapp.userexception.UserNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	private final VerificationTokenRepository verificationTokenRepository;

	private final PasswordEncoder passwordEncoder;

	private final MailServiceImpl mailServiceImpl;

	private final JwtService jwtService;

	@Override
	public User registerUser(UserRegistrationDto userRegistrationDto) {
		Optional<User> existingUser = userRepository.findByEmail(userRegistrationDto.getEmail());
		if(existingUser.isPresent()) {
			throw new UserAlreadyExistsException("User with email "+userRegistrationDto.getEmail()+" is already registered.");
		}

		// Save User with inactive status
		User newUser = User.builder().userName(userRegistrationDto.getUserName()).email(userRegistrationDto.getEmail())
				.password(passwordEncoder.encode(userRegistrationDto.getPassword()))
				.roles(userRegistrationDto.getRoles()).isActive(false).build();
		User savedUser = userRepository.save(newUser);

		// Generate Verification Token
		String token = UUID.randomUUID().toString();

		// Save Token
		VerificationToken verificationToken = VerificationToken.builder().token(token).creationDateTime(LocalDateTime.now())
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
	public User getUserByEmail(String authHeader) {
		String username = jwtService.getUsernameFromAuthHeader(authHeader);
		Optional<User> user = userRepository.findByEmail(username);
		if (user.isEmpty()) {
			throw new UserNotFoundException("Requested user does not exist.");
		}
		return user.get();
	}

	@Override
	public String deleteUserByEmail(String authHeader) {
		String username = jwtService.getUsernameFromAuthHeader(authHeader);
		Optional<User> user = userRepository.findByEmail(username);
		if (user.isEmpty()) {
			throw new UserNotFoundException("Requested user does not exist.");
		}
		User existingUser = user.get();
		userRepository.deleteById(existingUser.getUserId());
		return existingUser.getEmail();
	}

	@Override
	public User updateUserByEmail(UserUpdateDto userUpdateDto, String authHeader) {
		String username = jwtService.getUsernameFromAuthHeader(authHeader);
		Optional<User> user = userRepository.findByEmail(username);
		if (user.isEmpty()) {
			throw new UserNotFoundException("Requested user does not exist.");
		}
		User existingUser = user.get();
		existingUser.builder().userName(userUpdateDto.getUserName()).email(userUpdateDto.getEmail()).build();
		User savedUser = userRepository.save(existingUser);
		return savedUser;
	}

	@Override
	public void changePassword(ChangePasswordDto changePasswordDto, String authHeader) {
		String username = jwtService.getUsernameFromAuthHeader(authHeader);
		Optional<User> user = userRepository.findByEmail(username);
		if (user.isEmpty()) {
			throw new UserNotFoundException("Requested user does not exist.");
		}

		User existingUser = user.get();
		if (!passwordEncoder.matches(changePasswordDto.getCurrentPassword(), existingUser.getPassword())) {
			throw new IncorrectCurrentPasswordException("Given current password is incorrect.");
		}
		existingUser.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
		userRepository.save(existingUser);

	}

}
