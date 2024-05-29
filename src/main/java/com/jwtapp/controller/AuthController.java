package com.jwtapp.controller;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jwtapp.dto.LoginRequest;
import com.jwtapp.jwtconfig.JwtService;
import com.jwtapp.user.CustomUserDetailsService;
import com.jwtapp.user.User;
import com.jwtapp.user.UserRepository;
import com.jwtapp.verificationtoken.VerificationToken;
import com.jwtapp.verificationtoken.VerificationTokenRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

	private final JwtService jwtService;

	private final AuthenticationManager authenticationManager;

	private final CustomUserDetailsService customUserDetailsService;
	
	private final VerificationTokenRepository verificationTokenRepository;

	private final UserRepository userRepository;
	
	
	
	@Transactional
	@GetMapping("/verify/{token}")
	public String verifyUser(@PathVariable String token) {

		VerificationToken verificationToken = verificationTokenRepository.findByToken(token);

		LocalDateTime now = LocalDateTime.now();
		LocalDateTime expiryWithBuffer = verificationToken.getExpiryDate().plusSeconds(60);

		if (verificationToken == null || now.isAfter(expiryWithBuffer)) {
			return "Invalid or expired verification token.";
		}
		User user = verificationToken.getUser();
		user.setActive(true);
		userRepository.save(user);
		verificationTokenRepository.deleteById(verificationToken.getId());
		return "Email verified successfully.";
	}

	@PostMapping("/login")
	public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest) {

		// authenticating the username and password with database
		doAuthenticate(loginRequest.getUserName(), loginRequest.getPassword());

		// if authenticated get the user by username
		UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginRequest.getUserName());
		

		// Now generate the JWT token
		String jwtToken = jwtService.generateToken(userDetails);
		return new ResponseEntity<Object>(jwtToken, HttpStatus.OK);
	}

	private void doAuthenticate(String email, String password) {
		try {
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email,
					password);
			authenticationManager.authenticate(authenticationToken);
		} catch (BadCredentialsException e) {
			throw new BadCredentialsException("Invalid Username or Password !!");
		}
		catch (DisabledException e) {
			throw new DisabledException("Please verify the email !!");
		}
	}

}
