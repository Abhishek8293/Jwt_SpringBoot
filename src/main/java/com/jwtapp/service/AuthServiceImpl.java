package com.jwtapp.service;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.jwtapp.authexception.verificationTokenExpiredException;
import com.jwtapp.dto.LoginRequestDto;
import com.jwtapp.jwtconfig.JwtService;
import com.jwtapp.mail.MailServiceImpl;
import com.jwtapp.response.ResponseHandler;
import com.jwtapp.user.CustomUserDetailsService;
import com.jwtapp.user.User;
import com.jwtapp.user.UserRepository;
import com.jwtapp.verificationtoken.VerificationToken;
import com.jwtapp.verificationtoken.VerificationTokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	private final JwtService jwtService;

	private final AuthenticationManager authenticationManager;

	private final CustomUserDetailsService customUserDetailsService;

	private final VerificationTokenRepository verificationTokenRepository;

	private final UserRepository userRepository;

	private final MailServiceImpl mailServiceImpl;

	public String login(LoginRequestDto loginRequest) {
		// authenticating the username and password with database
		doAuthenticate(loginRequest.getUserName(), loginRequest.getPassword());
		// if authenticated get the user by username
		UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginRequest.getUserName());
		// Now generate the JWT token
		String jwtToken = jwtService.generateToken(userDetails);
		return jwtToken;
	}

	private void doAuthenticate(String email, String password) {
		try {
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email,
					password);
			authenticationManager.authenticate(authenticationToken);
		} catch (BadCredentialsException e) {
			throw new BadCredentialsException("Invalid Username or Password !!");
		} catch (DisabledException e) {
			throw new DisabledException("Please verify your email : " + email + "!!");
		}
	}

	public void verifyUser(String token) {
		VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime expiryWithBuffer = verificationToken.getExpiryDate().plusSeconds(60);

		if (verificationToken == null || now.isAfter(expiryWithBuffer)) {
			throw new verificationTokenExpiredException("Verification Token Invalid or Expired.");
		}
		User user = verificationToken.getUser();
		user.setActive(true);
		userRepository.save(user);
		verificationTokenRepository.deleteById(verificationToken.getId());
	}

	public void resendVerificationEmail() {
		mailServiceImpl.sendVerificationMail(null, null);
	}

}
