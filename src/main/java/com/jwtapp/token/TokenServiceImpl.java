package com.jwtapp.token;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jwtapp.user.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl {

	private final TokenRepository tokenRepository;

	public Token saveUserToken(User user, String jwtToken) {
		Token token = new Token();
		token.setJwtToken(jwtToken);
		token.setUser(user);
		token.setLoggedOut("false");
		Token savedToken = tokenRepository.save(token);
		return savedToken;
	}

	public void revokeAllTokenByUser(User user) {
		List<Token> validTokenList = tokenRepository.findAllTokenByUser(user.getUserId());
		if (!validTokenList.isEmpty()) {
			validTokenList.forEach(t -> {
				t.setLoggedOut("true");
			});
		}
		tokenRepository.saveAll(validTokenList);
	}

}
