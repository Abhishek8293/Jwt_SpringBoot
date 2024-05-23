package com.jwtapp.jwtconfig;

import java.util.List;

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
import com.jwtapp.token.Token;
import com.jwtapp.token.TokenRepository;
import com.jwtapp.user.User;
import com.jwtapp.user.UserRepository;

@RestController
@RequestMapping("/api/v1/auth")
public class JwtCotroller {

	private JwtService jwtService;

	private AuthenticationManager authenticationManager;

	private TokenRepository tokenRepository;

	private UserRepository userRepository;

	public JwtCotroller(JwtService jwtService, AuthenticationManager authenticationManager,
			TokenRepository tokenRepository, UserRepository userRepository) {
		super();
		this.jwtService = jwtService;
		this.authenticationManager = authenticationManager;
		this.tokenRepository = tokenRepository;
		this.userRepository = userRepository;
	}

	@PostMapping
	public ResponseEntity<Object> login(@RequestBody JwtRequest jwtRequest) {

		this.doAuthenticate(jwtRequest.getUsername(), jwtRequest.getPassword());
		String jwtToken = jwtService.generateToken(jwtRequest.getUsername());
		User user = userRepository.findByUserName(jwtRequest.getUsername());
		revokeAllTokenByUser(user);
		saveUserToken(user, jwtToken);

		JwtResponse jwtResponse = new JwtResponse();
		jwtResponse.setTokenType("Bearer");
		jwtResponse.setToken(jwtToken);

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

	private void saveUserToken(User user, String jwtToken) {
		Token token = new Token();
		token.setJwtToken(jwtToken);
		token.setUser(user);
		token.setLoggedOut("false");
		tokenRepository.save(token);
	}

	private void revokeAllTokenByUser(User user) {
			List<Token> validTokenList = tokenRepository.findAllTokenByUser(user.getUserId());
			if (!validTokenList.isEmpty()) {
				validTokenList.forEach(t-> {t.setLoggedOut("true");
					});
			}
			tokenRepository.saveAll(validTokenList);
	}

}
