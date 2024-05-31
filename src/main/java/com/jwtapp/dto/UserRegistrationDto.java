package com.jwtapp.dto;

import com.jwtapp.entity.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRegistrationDto {
	
	@NotEmpty(message = "Username cannot be empty.")
	private String userName;
	
	@Email(message = "Please enter a valid email address.")
	@NotEmpty(message = "Email cannot be empty")
	private String email;
	
	@Size(min = 6, message = "Password must be atleast 6 characters.")
	@NotEmpty(message = "Password cannot be empty.")
	private String password;
	
	@NotNull(message = "Role cannot be null")
	private Role roles;

}
