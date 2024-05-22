package com.jwtapp.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jwtapp.dto.UserRegistrationDto;
import com.jwtapp.entity.User;
import com.jwtapp.repository.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepository;

	private PasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		super();
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public User registerUser(UserRegistrationDto userDto) {
		User user = new User();
		user.setUserName(userDto.getUserName());
		user.setEmail(userDto.getEmail());
		user.setPassword(passwordEncoder.encode(userDto.getPassword()));
		user.setRoles(userDto.getRoles());
		return userRepository.save(user);

	}

}
