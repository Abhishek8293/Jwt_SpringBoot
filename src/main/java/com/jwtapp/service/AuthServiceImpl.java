package com.jwtapp.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jwtapp.authexception.PasswordDoesNotMatchException;
import com.jwtapp.authexception.verificationTokenExpiredException;
import com.jwtapp.dto.ForgotPasswordDto;
import com.jwtapp.dto.LoginRequestDto;
import com.jwtapp.dto.ResetPasswordDto;
import com.jwtapp.entity.User;
import com.jwtapp.entity.VerificationToken;
import com.jwtapp.mail.MailServiceImpl;
import com.jwtapp.repository.JwtTokenRepository;
import com.jwtapp.repository.UserRepository;
import com.jwtapp.repository.VerificationTokenRepository;
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

	private final JwtTokenServiceImpl jwtTokenServiceImpl;

	private final UserRepository userRepository;

	private final MailServiceImpl mailServiceImpl;

	private final PasswordEncoder passwordEncoder;

	@Override
	public String login(LoginRequestDto loginRequest) {
		User existingUser = userRepository.findByEmail(loginRequest.getUserName())
				.orElseThrow(() -> new UserNotFoundException("Incorrect Username."));

		if (passwordEncoder.matches(loginRequest.getPassword(), existingUser.getPassword())) {
			doAuthenticate(loginRequest.getUserName(), loginRequest.getPassword());
		} else {
			throw new PasswordDoesNotMatchException("Incorrect Password.");
		}
		UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginRequest.getUserName());
		// Now generate the JWT token
		String jwtToken = jwtService.generateToken(userDetails);
		jwtTokenServiceImpl.setAllJwtTokenLoggedOut(userDetails);
		jwtTokenServiceImpl.saveJwtToken(userDetails, jwtToken);
		return jwtToken;
	}

	private void doAuthenticate(String email, String password) {
		try {
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
	public void verifyUserByEmail(String token) {
		VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime expiryWithBuffer = verificationToken.getCreationDateTime().plusSeconds(60);

		if (verificationToken == null || now.isAfter(expiryWithBuffer)) {
			throw new verificationTokenExpiredException("Verification Token Invalid or Expired.");
		}
		User user = verificationToken.getUser();
		user.setActive(true);
		userRepository.save(user);
		verificationTokenRepository.deleteById(verificationToken.getId());
	}

	@Override
	public void resendUserRegistrationVerificationEmail(String email) {
		User existingUser = userRepository.findByEmail(email)
				.orElseThrow(() -> new UserNotFoundException("Please providen an already registerd email."));
		// deleting the already existed verification token.
		verificationTokenRepository.deleteByUserId(existingUser.getUserId());
		// Generate Verification Token
		String token = UUID.randomUUID().toString();
		// Save Token
		VerificationToken verificationToken = VerificationToken.builder().token(token)
				.creationDateTime(LocalDateTime.now()).user(existingUser).build();
		verificationTokenRepository.save(verificationToken);
		mailServiceImpl.sendUserRegistrationVerificationMail(existingUser, token);

	}

	@Override
	public void forgotPassword(ForgotPasswordDto forgotPasswordDto) {
		User existingUser = userRepository.findByEmail(forgotPasswordDto.getEmail())
				.orElseThrow(() -> new UserNotFoundException("Please providen an already registerd email."));
		// Generate Verification Token
		String token = UUID.randomUUID().toString();
		// Save Token
		VerificationToken verificationToken = VerificationToken.builder().token(token)
				.creationDateTime(LocalDateTime.now()).user(existingUser).build();
		verificationTokenRepository.save(verificationToken);
		// Send reset password Mail
		mailServiceImpl.sendForgotPasswordVerificationMail(existingUser, token);
	}

	@Override
	public void verifyResetPasswordEmail(String token) {
		VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime expiryWithBuffer = verificationToken.getCreationDateTime().plusSeconds(60);
		if (verificationToken == null || now.isAfter(expiryWithBuffer)) {
			throw new verificationTokenExpiredException("Verification Token Invalid or Expired.");
		}
	}

	public void updatePassword(ResetPasswordDto resetPasswordDto) {
		VerificationToken verificationToken = verificationTokenRepository.findByToken(resetPasswordDto.getToken());
		// find the user using the token
		User user = verificationToken.getUser();
		// Hash and set the new hashed password to user
		user.setPassword(passwordEncoder.encode(resetPasswordDto.getNewPassword()));
		// save the user with new hashed password
		userRepository.save(user);
		// delete the token
		verificationTokenRepository.deleteById(verificationToken.getId());
	}

}
