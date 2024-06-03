package com.jwtapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ForgotPasswordDto {
	
	@Email(message = "Enter a valid email")
	@NotEmpty(message = "Email cannot be empty.")
	private String email;

}
