package com.jwtapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jwtapp.dto.LoginRequest;
import com.jwtapp.jwtconfig.JwtService;
import com.jwtapp.user.CustomUserDetailsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/login")
public class AuthController {

	private final JwtService jwtService;

	private final AuthenticationManager authenticationManager;
	
	private final CustomUserDetailsService customUserDetailsService;

	@PostMapping
	public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest) {
		doAuthenticate(loginRequest.getUserName(), loginRequest.getPassword());
		UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginRequest.getUserName());
		String jwtToken = jwtService.generateToken(userDetails);
		return new ResponseEntity<Object>(jwtToken, HttpStatus.OK);
	}

	private void doAuthenticate(String email, String password) {
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email,
				password);
		authenticationManager.authenticate(authentication);
	}

}
