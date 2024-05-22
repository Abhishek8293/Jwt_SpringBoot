package com.jwtapp.dao;

import java.util.Optional;

import com.jwtapp.entity.User;

public interface UserDao {
	
	public void saveUser(User user);
	public User findUser(String email);

}