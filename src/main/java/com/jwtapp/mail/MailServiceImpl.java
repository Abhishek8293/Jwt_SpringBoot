package com.jwtapp.mail;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.jwtapp.user.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

	private final JavaMailSender javaMailSender;

	@Override
	public void sendVerificationMail(User user, String token) {
		String url = "http://localhost:8080/verify/" + token;
		String message = "Click the following link to verify your email: " + url;
		sendSimpleMessage(user.getEmail(), "Email Verification", message);
	}

	@Override
	public void sendSimpleMessage(String to, String subject, String text) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject(subject);
		message.setText(text);
		javaMailSender.send(message);
	}

}
