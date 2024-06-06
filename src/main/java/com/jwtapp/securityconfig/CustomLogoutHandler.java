package com.jwtapp.securityconfig;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import com.jwtapp.entity.JwtToken;
import com.jwtapp.repository.JwtTokenRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {

	private final JwtTokenRepository jwtTokenRepository;

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

		String requestHeader = request.getHeader("Authorization");
		String jwtToken = null;
		if (requestHeader != null && requestHeader.startsWith("Bearer")) {
			jwtToken = requestHeader.substring(7);
		} else {
			return;
		}
		JwtToken storedJwtToken = jwtTokenRepository.findByJwtToken(jwtToken).orElse(null);
		if (storedJwtToken != null) {
			storedJwtToken.setLoggedOut(true);
			jwtTokenRepository.save(storedJwtToken);
		}

		response.setStatus(HttpStatus.OK.value());
		response.setContentType("application/json");
		try {
			response.getWriter().write("Successfully logged out");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
