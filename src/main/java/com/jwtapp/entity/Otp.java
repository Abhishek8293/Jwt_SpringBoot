package com.jwtapp.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "user_verification_otp_table")
@AllArgsConstructor
@NoArgsConstructor
public class Otp {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer otpId;

	@Column(nullable = false)
	private String otpNumber;

	@Column(nullable = false)
	private LocalDateTime otpCreationTime;

	@ManyToOne
	@JoinColumn(name = "user_id_fk")
	private User user;

}
