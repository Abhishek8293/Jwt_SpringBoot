package com.jwtapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.jwtapp.entity.JwtToken;
import com.jwtapp.entity.User;
import com.jwtapp.repository.JwtTokenRepository;
import com.jwtapp.repository.UserRepository;
import com.jwtapp.userexception.UserNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtTokenServiceImpl implements JwtTokenService {

	private final JwtTokenRepository jwtTokenRepository;

	private final UserRepository userRepository;

	@Override
	public JwtToken saveJwtToken(UserDetails userDetails, String jwtToken) {

		Optional<User> user = userRepository.findByEmail(userDetails.getUsername());
		if (user.isEmpty()) {
			throw new UserNotFoundException("Requested user does not exists.");
		}
		JwtToken newJwtToken = new JwtToken();
		newJwtToken.setJwtToken(jwtToken);
		newJwtToken.setLoggedOut(false);
		newJwtToken.setUser(user.get());
		return jwtTokenRepository.save(newJwtToken);
	}

	@Override
	public void setAllJwtTokenLoggedOut(UserDetails userDetails) {
		Optional<User> user = userRepository.findByEmail(userDetails.getUsername());
		List<JwtToken> validJwtTokens = jwtTokenRepository.findAllTokenByUser(user.get().getUserId());
		if (!validJwtTokens.isEmpty()) {
			validJwtTokens.forEach(t -> {
				t.setLoggedOut(true);
			});
		}
		jwtTokenRepository.saveAll(validJwtTokens);
	}

}
