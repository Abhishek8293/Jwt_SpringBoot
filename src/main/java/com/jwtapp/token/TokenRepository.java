package com.jwtapp.token;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {

	@Query("""
			Select t from Token t inner join User u
			on t.user.userId = u.userId
			where t.user.userId = :userId and t.loggedOut = "false"
			""")

	List<Token> findAllTokenByUser(Integer userId);

	Optional<Token> findByJwtToken(String jwtToken);
}
