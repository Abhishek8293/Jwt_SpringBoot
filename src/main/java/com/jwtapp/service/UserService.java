package com.jwtapp.service;

import com.jwtapp.dto.UserRegistrationDto;
import com.jwtapp.user.User;

public interface UserService {
	
	User registerUser(UserRegistrationDto userRegistrationDto);

}
