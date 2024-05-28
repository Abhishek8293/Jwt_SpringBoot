package com.jwtapp.verificationtoken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Integer> {

	VerificationToken findByToken(String token);

	void deleteByToken(VerificationToken verificationToken);

}