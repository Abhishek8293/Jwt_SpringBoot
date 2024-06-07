package com.jwtapp.service;

import com.jwtapp.entity.User;

import jakarta.mail.MessagingException;

public interface MailService {

	void sendSimpleMessage(String to, String subject, String text);

	void sendeMimeMessage(String to, String subject, String text) throws MessagingException;

	void sendUserRegistrationVerificationMail(User user, String token);

	void sendForgotPasswordVerificationMail(User user, String token);

}
