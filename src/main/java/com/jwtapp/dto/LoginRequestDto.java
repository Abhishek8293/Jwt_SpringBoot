package com.jwtapp.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRequestDto {
	
	@NotEmpty(message = "Username cannot be empty.")
	private String userName;
	
	@NotEmpty(message = "Password cannot be empty")
	private String password;

}
