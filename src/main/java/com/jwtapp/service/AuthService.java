package com.jwtapp.service;

import com.jwtapp.dto.LoginRequestDto;

public interface AuthService {
	
	String login(LoginRequestDto loginRequest);
	
	void verifyUser(String token);
	
	void resendVerificationEmail(String email);

}
