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
		User existingUser = userRepository.findByEmail(userRegistrationDto.getEmail())
				.orElseThrow(() -> new UserAlreadyExistsException(
						"User with email " + userRegistrationDto.getEmail() + " is already registered."));

		// Save User with inactive status
		User newUser = User.builder().userName(userRegistrationDto.getUserName()).email(userRegistrationDto.getEmail())
				.password(passwordEncoder.encode(userRegistrationDto.getPassword()))
				.roles(userRegistrationDto.getRoles()).isActive(false).build();
		User savedUser = userRepository.save(newUser);

		// Generate Verification Token
		String token = UUID.randomUUID().toString();

		// Save Token
		VerificationToken verificationToken = VerificationToken.builder().token(token)
				.creationDateTime(LocalDateTime.now()).user(newUser).build();
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
		User existingUser = userRepository.findByEmail(username)
				.orElseThrow(() -> new UserNotFoundException("Requested user does not exist."));
		return existingUser;
	}

	@Override
	public String deleteUserByEmail(String authHeader) {
		String username = jwtService.getUsernameFromAuthHeader(authHeader);
		User existingUser = userRepository.findByEmail(username)
				.orElseThrow(() -> new UserNotFoundException("Requested user does not exist."));
		userRepository.deleteById(existingUser.getUserId());
		return existingUser.getEmail();
	}

	@Override
	public User updateUserByEmail(UserUpdateDto userUpdateDto, String authHeader) {
		String username = jwtService.getUsernameFromAuthHeader(authHeader);
		User existingUser = userRepository.findByEmail(username)
				.orElseThrow(() -> new UserNotFoundException("Requested user does not exist."));
		existingUser.setUserName(userUpdateDto.getUserName());
		existingUser.setEmail(userUpdateDto.getEmail());
		User savedUser = userRepository.save(existingUser);
		return savedUser;
	}

	@Override
	public void changeUserPassword(ChangePasswordDto changePasswordDto, String authHeader) {
		String username = jwtService.getUsernameFromAuthHeader(authHeader);
		User existingUser = userRepository.findByEmail(username)
				.orElseThrow(() -> new UserNotFoundException("Requested user does not exist."));
		if (!passwordEncoder.matches(changePasswordDto.getCurrentPassword(), existingUser.getPassword())) {
			throw new IncorrectCurrentPasswordException("Given current password is incorrect.");
		}
		existingUser.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
		userRepository.save(existingUser);

	}

}
