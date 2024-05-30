package com.jwtapp.service;

import com.jwtapp.dto.LoginRequest;

public interface AuthService {
	
	String login(LoginRequest loginRequest);
	
	void verifyUser(String token);

}
