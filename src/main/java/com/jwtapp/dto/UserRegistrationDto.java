package com.jwtapp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationDto {
	
	@NotBlank(message = "user name can't be empty or null !!")
	private String userName;
	@NotBlank(message = "email can't be empty or null !!")
	private String email;
	@NotBlank(message = "Passwork can not be empty or null !!")
	private String password;
	
	private String roles;

}
