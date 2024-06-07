package com.jwtapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jwtapp.entity.Otp;
import java.util.Optional;


@Repository
public interface OtpRepository  extends JpaRepository<Otp, Integer>{
	
	Optional<Otp> findByOtpNumber(String otpNumber);

}
