package com.jwtapp.jwtconfig;

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

import com.jwtapp.user.Role;

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

	// extract role from token
	public Role getRolesFromToken(String token) {
		Claims claims = getAllClaims(token);
		List<Object> roles = (List<Object>) claims.get("roles");

		if (roles == null || roles.isEmpty()) {
			return null; // Handle case where no roles are present
		}

		// Assuming there's only one role object (modify if different)
		Map<String, Object> roleMap = (Map<String, Object>) roles.get(0);
		String authority = (String) roleMap.get("authority");

		return Role.valueOf(authority);
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
		Role userRole = getRolesFromToken(token);
		Role userDetailRole = getRoleFromUserDetails(userDetails); // New method to extract role from userDetails

		return (userNameFromToken.equals(userDetails.getUsername()) && !isTokenExpired(token) && userRole != null
				&& userDetailRole != null && userRole.equals(userDetailRole));
	}

	// Helper method to extract role from UserDetails (assuming a single role)
	private Role getRoleFromUserDetails(UserDetails userDetails) {
		List<GrantedAuthority> authorities = (List<GrantedAuthority>) userDetails.getAuthorities();
		if (authorities.isEmpty()) {
			return null; // Handle case where no authorities are present (unlikely)
		}
		GrantedAuthority authority = authorities.get(0); // Assuming single role, modify if needed
		return Role.valueOf(authority.getAuthority());
	}

}
