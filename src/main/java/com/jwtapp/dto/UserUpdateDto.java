package com.jwtapp.dto;

import com.jwtapp.user.Role;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserUpdateDto {

	@NotEmpty(message = "Username cannot be empty")
	private String userName;

	@Email(message = "Please enter a valid email address")
	private String email;

	@Nullable // Allow password update to be optional
	@Size(min = 6, message = "Password must be at least 6 characters")
	private String password;

	@NotNull(message = "Role cannot be null")
	private Role roles;

}
