package com.jwtapp.dao;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.jwtapp.entity.User;
import com.jwtapp.repository.UserRepository;

public class UserDaoImpl implements UserDao {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public void saveUser(User user) {
		userRepository.save(user);
	}

	@Override
	public User findUser(String email)throws UsernameNotFoundException {
		return userRepository.findByUserName(email);
	}


}
