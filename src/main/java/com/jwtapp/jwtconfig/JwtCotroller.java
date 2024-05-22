package com.jwtapp.jwtconfig;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jwtapp.response.ResponseHandler;

@RestController
@RequestMapping("/api/v1/auth")
public class JwtCotroller {

	private JwtService jwtService;

	private AuthenticationManager authenticationManager;

	public JwtCotroller(JwtService jwtService, AuthenticationManager authenticationManager) {
		super();
		this.jwtService = jwtService;
		this.authenticationManager = authenticationManager;
	}

	@PostMapping
	public ResponseEntity<Object> login(@RequestBody JwtRequest jwtRequest) {

		this.doAuthenticate(jwtRequest.getUsername(), jwtRequest.getPassword());
		String token = jwtService.generateToken(jwtRequest.getUsername());
		JwtResponse jwtResponse = new JwtResponse();
		jwtResponse.setTokenType("Bearer");
		jwtResponse.setToken(token);
		return ResponseHandler.responseBuilder("Token is successfully generated.", HttpStatus.OK, jwtResponse);
	}

	// to authenticate the given username and password before generating the
	// token
	private void doAuthenticate(String username, String password) {
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username,
				password);
		try {
			authenticationManager.authenticate(authentication);
		} catch (BadCredentialsException e) {
			throw new BadCredentialsException("Credentials Invalid !!");
		}

	}

}
