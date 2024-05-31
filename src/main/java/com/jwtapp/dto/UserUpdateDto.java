package com.jwtapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserUpdateDto {

	@NotEmpty(message = "Username cannot be empty")
	private String userName;

	@Email(message = "Please enter a valid email address")
	@NotEmpty(message = "Email cannot be empty")
	private String email;

}
