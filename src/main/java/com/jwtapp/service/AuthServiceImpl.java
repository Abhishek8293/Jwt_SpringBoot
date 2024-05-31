package com.jwtapp.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.jwtapp.authexception.verificationTokenExpiredException;
import com.jwtapp.dto.LoginRequestDto;
import com.jwtapp.entity.User;
import com.jwtapp.entity.VerificationToken;
import com.jwtapp.mail.MailServiceImpl;
import com.jwtapp.repository.UserRepository;
import com.jwtapp.repository.VerificationTokenRepository;
import com.jwtapp.response.ResponseHandler;
import com.jwtapp.securityconfig.CustomUserDetailsService;
import com.jwtapp.securityconfig.JwtService;
import com.jwtapp.userexception.UserNotFoundException;

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

	private final CustomUserDetailsService userDetailsService;

	@Override
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
//			UserDetails userDetails = userDetailsService.loadUserByUsername(email);
//			if (!userDetails.isEnabled()) {
//				throw new DisabledException("Please verify your email: " + email + "!!");
//			}
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email,
					password);
			authenticationManager.authenticate(authenticationToken);
		} catch (BadCredentialsException e) {
			throw new BadCredentialsException("Incorrect Username or Password !!");
		} catch (DisabledException e) {
			throw new DisabledException("Please verify your email : " + email + "!!");
		}
	}

	@Override
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

	@Override
	public void resendVerificationEmail(String email) {
		Optional<User> user = userRepository.findByEmail(email);
		if (user.isEmpty()) {
			throw new UserNotFoundException("Please provide an already registerd email.");
		}

		User existingUser = user.get();
		// deleting the already existed verification token.
		verificationTokenRepository.deleteByUserId(existingUser.getUserId());
		// Generate Verification Token
		String token = UUID.randomUUID().toString();
		// Save Token
		VerificationToken verificationToken = VerificationToken.builder().token(token).expiryDate(LocalDateTime.now())
				.user(existingUser).build();
		verificationTokenRepository.save(verificationToken);
		mailServiceImpl.sendVerificationMail(existingUser, token);

	}

}
