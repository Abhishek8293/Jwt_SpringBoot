package com.jwtapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.jwtapp.entity.JwtToken;

@Repository
public interface JwtTokenRepository extends JpaRepository<JwtToken, Integer> {
	
	@Query("Select jwt from JwtToken jwt where jwt.user.id = :userId and jwt.isLoggedOut = false")
	List<JwtToken> findAllTokenByUser(Integer userId);
	
	Optional<JwtToken>  findByJwtToken(String jwtToken);

}
