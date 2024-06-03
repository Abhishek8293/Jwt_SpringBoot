package com.jwtapp.service;

import org.springframework.security.core.userdetails.UserDetails;

import com.jwtapp.entity.JwtToken;

public interface JwtTokenService {
	
	JwtToken saveJwtToken(UserDetails userDetails,String jwtToken);
	
	void setAllJwtTokenLoggedOut(UserDetails userDetails);

}
