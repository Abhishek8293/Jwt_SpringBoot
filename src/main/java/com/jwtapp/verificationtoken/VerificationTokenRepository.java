package com.jwtapp.verificationtoken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Integer> {

	VerificationToken findByToken(String token);

	
	@Modifying
    @Transactional
    @Query("DELETE FROM VerificationToken v WHERE v.user.userId = :userId")
    void deleteByUserId(Integer userId);

}
