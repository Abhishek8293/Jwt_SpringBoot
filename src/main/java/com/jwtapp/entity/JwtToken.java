package com.jwtapp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "jwt_token_table")
public class JwtToken {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer jwtTokenId;
	
	private String jwtToken;
	
	private boolean isLoggedOut;
	
	@ManyToOne
	@JoinColumn(name = "user_id_fk")
	private User user;

}
