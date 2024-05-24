package com.jwtapp.securityconfig;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import com.jwtapp.exception.TokenNotFoundException;
import com.jwtapp.token.Token;
import com.jwtapp.token.TokenRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {

	private final TokenRepository tokenRepository;

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		String requestHeader = request.getHeader("Authorization");
		String jwtToken = null;
		if (requestHeader != null && requestHeader.startsWith("Bearer")) {
			jwtToken = requestHeader.substring(7);
		}

		Optional<Token> storedToken = tokenRepository.findByJwtToken(jwtToken);
		if (storedToken.isEmpty()) {
			throw new TokenNotFoundException("Token not found !!!");
		}
		Token updatedToken = storedToken.get();
		tokenRepository.save(updatedToken);

	}

}
