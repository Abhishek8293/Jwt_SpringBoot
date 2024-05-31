package com.jwtapp.service;

import java.util.List;

import com.jwtapp.dto.ChangePasswordDto;
import com.jwtapp.dto.UserRegistrationDto;
import com.jwtapp.dto.UserUpdateDto;
import com.jwtapp.entity.User;

public interface UserService {
	
	User registerUser(UserRegistrationDto userRegistrationDto);
	
	List<User> getAllUser();
	
	User getUserByEmail(String email);
	
	String deleteUserByEmail(String authHeader);
	
	User updateUserByEmail(UserUpdateDto userUpdateDto, String authHeader);
	
	void changePassword(ChangePasswordDto changePasswordDto, String  authHeader);

}
