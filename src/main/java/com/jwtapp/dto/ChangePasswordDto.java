package com.jwtapp.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChangePasswordDto {
	
	@Size(min = 6, message = "Password must be atleast 6 characters.")
	@NotEmpty(message = "Current password cannot be empty.")
	private String currentPassword;
	
	@Size(min = 6, message = "Password must be atleast 6 characters.")
	@NotEmpty(message = "New password cannot be empty.")
	private String newPassword;

}
