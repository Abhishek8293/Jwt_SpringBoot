package com.jwtapp.user;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jwtapp.dto.UserRegistrationDto;
import com.jwtapp.exception.UserAlreadyExistsException;
import com.jwtapp.exception.UserNotFoundException;
import com.jwtapp.jwtconfig.JwtService;
import com.jwtapp.token.Token;
import com.jwtapp.token.TokenServiceImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;

	private final JwtService jwtService;

	private final TokenServiceImpl tokenServiceImpl;

	public User registerUser(UserRegistrationDto userDto) {

		Optional<User> alreadyExistingUser = userRepository.findByUserName(userDto.getUserName());
		if (alreadyExistingUser.isPresent()) {
			throw new UserAlreadyExistsException("Username " + userDto.getUserName() + " is already registered");
		}

		User newUser = new User();
		newUser.setUserName(userDto.getUserName());
		newUser.setEmail(userDto.getEmail());
		newUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
		newUser.setRoles(userDto.getRoles());
		User savedUser = userRepository.save(newUser);
		String jwtToken = jwtService.generateToken(savedUser.getUserName());
		Token savedToken = tokenServiceImpl.saveUserToken(savedUser, jwtToken);
		savedUser.setToken(Arrays.asList(savedToken));

		return savedUser;
	}

	public User findUser(Integer userId) {
		Optional<User> user = userRepository.findById(userId);
		if (user.isEmpty()) {
			throw new UserNotFoundException("Requested user does not exist. !!");
		}
		return user.get();
	}

}
