package com.jwtapp.mail;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.jwtapp.entity.User;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

	private final JavaMailSender javaMailSender;

	@Override
	public void sendVerificationMail(User user, String token) {
		String url = "http://localhost:8080/auth/verify/" + token;
		// String message = "Click the following link to verify your email: " + url;
		// sendSimpleMessage(user.getEmail(), "Email Verification", message);

		String htmlBody = "<!DOCTYPE html>" + 
				"<html>" + 
				"<head>" +
				"<meta charset=\"UTF-8\">" +
				"<title>Email Verification</title>" +
				"<style>body{font-family:sans-serif;}</style>"+
				"</head>" +
				"<body>" +
				"<p>Dear <b>"+user.getUserName()+"</b>"+
				"<p>We just need to verify your email address before you can access <b>Company Name.</b>"+
				"<p>Please <a href=" +url+ "><b>Click Here<b/> </a>to verify your email.</p>" +
				"</body>" +
				"</html>";

		try {
			sendeMimeMessage(user.getEmail(), "Email Verification", htmlBody);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void sendSimpleMessage(String to, String subject, String text) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject(subject);
		message.setText(text);
		javaMailSender.send(message);
	}

	@Override
	public void sendeMimeMessage(String to, String subject, String htmlBody) throws MessagingException {

		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);

		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(htmlBody, true);
		javaMailSender.send(message);

	}

}
