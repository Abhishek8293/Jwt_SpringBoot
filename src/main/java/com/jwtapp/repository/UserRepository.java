package com.jwtapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jwtapp.entity.User;

@Repository
public interface UserRepository  extends JpaRepository<User,Integer>{

	User findByUserName(String email);

}