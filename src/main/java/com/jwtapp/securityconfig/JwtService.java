package com.jwtapp.securityconfig;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.jwtapp.entity.Role;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtService {

	// expiration time
	public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60; // This 5hr*60 minutes *60 seconds

	// private key is also encrypted for one more security layer
	private final String SECRETE = "gq4p9AA5M7V152IWFBuHCk1eUYsyFUFqiiDetlqoCxpXgCBLx2B8bkGLk3UJmIWKgOanTF72P/nEswX21boioDr/d0jakqrmEYE9LSCoLmisDT9VM3vUvOOwcta9YBJa4mzurpFrvYy8X+AD63wQhdyExEMboRt8LH478imFnotL6m4Xz3ZrqomwOdHQzX8Et6SnHTUNehgzIKpL+9S30xS7Xzet0JbXb0Z5F6rH2Lz3HslMsgKedAsf6Ztp64Z/N5yJaaKaF6kS4kJh//L8eQSOdmxFTK7K8W4r+rzn/mUG8iZFx2QLV1ReBzX+YtOAN1/BRpHDXrXP8Tmv/P8aUM3T1bggzPgrXp7rejQckf0=";

	// extract user-name from token
	public String getUsernameFromToken(String token) {
		return getClaimsFromToken(token, Claims::getSubject);
	}

	// extract expiration date from token
	public Date getExpirationDateFromToken(String token) {
		return getClaimsFromToken(token, Claims::getExpiration);
	}

	// To extract the claims in generic way
	private <T> T getClaimsFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaims(token);
		return claimsResolver.apply(claims);
	}

	// To extract all the claims from the token
	private Claims getAllClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
	}

	// To check whether the token is expired or not
	private Boolean isTokenExpired(String token) {
		final Date expirationDate = getExpirationDateFromToken(token);
		return expirationDate.before(new Date());
	}

	// public method to generate a token
	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("roles", userDetails.getAuthorities());
		return createToken(userDetails.getUsername(), claims);
	}

	// create a token using all the necessary details
	private String createToken(String username, Map<String, Object> claims) {
		return Jwts.builder().setClaims(claims).setSubject(username).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
				.signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
	}

	// To decode the secrete key
	private Key getSignKey() {
		byte[] signkey = Decoders.BASE64.decode(SECRETE);
		return Keys.hmacShaKeyFor(signkey);
	}

	// To validate the token
	public Boolean validateToken(String token, UserDetails userDetails) {
		final String userNameFromToken = getUsernameFromToken(token);
		return (userNameFromToken.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

}
