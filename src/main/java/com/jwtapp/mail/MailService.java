package com.jwtapp.mail;

import com.jwtapp.entity.User;

import jakarta.mail.MessagingException;

public interface MailService {
	
	void sendSimpleMessage(String to,String subject,String text);
	
	void sendeMimeMessage(String to,String subject,String text) throws MessagingException;
	
	void sendVerificationMail(User user,String token);
	
	

}
