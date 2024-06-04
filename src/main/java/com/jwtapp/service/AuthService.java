package com.jwtapp.service;

import com.jwtapp.dto.ForgotPasswordDto;
import com.jwtapp.dto.LoginRequestDto;
import com.jwtapp.dto.ResetPasswordDto;
import com.jwtapp.entity.User;

public interface AuthService {
	
	String login(LoginRequestDto loginRequest);
	
	void verifyUserByEmail(String token);
	
	void resendUserRegistrationVerificationEmail(String email);
	
	void forgotPassword(ForgotPasswordDto forgotPasswordDto);
	
	void verifyResetPasswordEmail(String token);
	
	void updatePassword( ResetPasswordDto resetPasswordDto);

}
