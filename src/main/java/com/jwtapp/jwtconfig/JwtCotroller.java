package com.jwtapp.jwtconfig;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.token.TokenService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jwtapp.response.ResponseHandler;
import com.jwtapp.token.TokenServiceImpl;
import com.jwtapp.user.User;
import com.jwtapp.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class JwtCotroller {

	private final JwtService jwtService;

	private final UserRepository userRepository;

	private final AuthenticationManager authenticationManager;

	private final TokenServiceImpl tokenServiceImpl;

	@PostMapping
	public ResponseEntity<Object> login(@RequestBody JwtRequest jwtRequest) {

		doAuthenticate(jwtRequest.getUsername(), jwtRequest.getPassword());
		String jwtToken = jwtService.generateToken(jwtRequest.getUsername());
		User user = userRepository.findByUserName(jwtRequest.getUsername()).get();
		tokenServiceImpl.revokeAllTokenByUser(user);
		tokenServiceImpl.saveUserToken(user, jwtToken);

		JwtResponse jwtResponse = new JwtResponse();
		jwtResponse.setTokenType("Bearer");
		jwtResponse.setToken(jwtToken);

		return ResponseHandler.responseBuilder("Token is successfully generated.", HttpStatus.OK, jwtResponse);
	}

	private void doAuthenticate(String username, String password) {
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username,
				password);
		authenticationManager.authenticate(authentication);
	}

}
