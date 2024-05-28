package com.jwtapp.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jwtapp.dto.UserRegistrationDto;
import com.jwtapp.user.User;
import com.jwtapp.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	
	private final UserRepository userRepository;
	
	private final PasswordEncoder passwordEncoder;

	@Override
	public User registerUser(UserRegistrationDto userRegistrationDto) {
		User newUser = User.builder()
				.userName(userRegistrationDto.getUserName())
				.email(userRegistrationDto.getEmail())
				.password(passwordEncoder.encode(userRegistrationDto.getPassword()))
				.roles(userRegistrationDto.getRoles())
				.build();
		User savedUser = userRepository.save(newUser);
		return savedUser;
	}

}
