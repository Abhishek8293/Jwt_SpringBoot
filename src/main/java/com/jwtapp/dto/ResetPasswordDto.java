package com.jwtapp.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResetPasswordDto {
	
	private String token;
	
	@NotNull
	@NotEmpty(message = "New Password Cannot Be Empty.")
	@Size(min = 6)
	private String newPassword;

}
