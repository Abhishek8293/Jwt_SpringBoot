package com.jwtapp.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveOtpDto {
	
	@NotEmpty(message = "otp can not be empty.")
	private String otpNumber;
	private String userName;

}
