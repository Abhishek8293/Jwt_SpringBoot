package com.jwtapp.mail;

import com.jwtapp.user.User;

public interface MailService {
	
	void sendSimpleMessage(String to,String subject,String text);
	
	void sendVerificationMail(User user,String token);

}
