package com.jwtapp.service;

import java.util.List;
import com.jwtapp.dto.UserRegistrationDto;
import com.jwtapp.dto.UserUpdateDto;
import com.jwtapp.user.User;

public interface UserService {
	
	User registerUser(UserRegistrationDto userRegistrationDto);
	
	List<User> getAllUser();
	
	User getUserByEmail(String email);
	
	void deleteUserByEmail(String email);
	
	User updateUserByEmail(UserUpdateDto userUpdateDto, String email);

}
