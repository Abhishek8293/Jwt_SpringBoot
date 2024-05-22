package com.jwtapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jwtapp.dao.UserDaoImpl;
import com.jwtapp.dto.UserRegistrationDto;
import com.jwtapp.entity.User;

@Service
public class UserService {
	
	@Autowired
	private UserDaoImpl userDaoImpl;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	public String registerUser(UserRegistrationDto userDto) {
				User user = new User();
				user.setUserName(userDto.getUserName());
				user.setEmail(userDto.getEmail());
				user.setPassword(passwordEncoder.encode(userDto.getPassword()));
				user.setRoles(userDto.getRoles());
				userDaoImpl.saveUser(user);
		 return "user "+userDto.getUserName()+" is successfully registered";
				
	}

}
