package com.jwtapp.service;

import com.jwtapp.dto.ForgotPasswordDto;
import com.jwtapp.dto.LoginRequestDto;
import com.jwtapp.entity.User;

public interface AuthService {
	
	String login(LoginRequestDto loginRequest);
	
	void verifyUser(String token);
	
	void resendVerificationEmail(String email);
	
	void forgotPassword(ForgotPasswordDto forgotPasswordDto);
	
	void verifyForgotPasswordEmail(String token);

}
