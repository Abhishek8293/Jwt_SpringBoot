package com.jwtapp.dto;

import com.jwtapp.user.Role;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRegistrationDto {
	
	private String userName;
	private String email;
	private String password;
	private Role roles;

}
