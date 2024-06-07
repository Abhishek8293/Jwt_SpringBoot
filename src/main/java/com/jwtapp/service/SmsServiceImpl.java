package com.jwtapp.service;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.jwtapp.dto.OtpRequestDto;
import com.jwtapp.dto.OtpVerificationDto;
import com.jwtapp.dto.SaveOtpDto;
import com.jwtapp.entity.Otp;
import com.jwtapp.entity.User;
import com.jwtapp.repository.OtpRepository;
import com.jwtapp.repository.UserRepository;
import com.jwtapp.smsTwilio.OtpExpiredException;
import com.jwtapp.smsTwilio.OtpNotFoundException;
import com.jwtapp.smsTwilio.SmsNotSentException;
import com.jwtapp.smsTwilio.TwilioConfig;
import com.jwtapp.userexception.UserNotFoundException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SmsServiceImpl implements SmsService {

	private final TwilioConfig twilioConfig;

	private final OtpRepository otpRepository;

	private final UserRepository userRepository;

	public Message sendSms(OtpRequestDto otpRequestDto) {
		Message message = null;
		String otp = null;
		try {
			PhoneNumber to = new PhoneNumber(otpRequestDto.getPhoneNumber());
			PhoneNumber from = new PhoneNumber(twilioConfig.getPhoneNumber());
			otp = generateOTP();
			String otpMessage = "Dear " + otpRequestDto.getUserName() + ", Your verification OTP : " + otp
					+ " for [COMPANY-NAME] Valid for next 2 minutes. Please do not share the OTP.";
			message = Message.creator(to, from, otpMessage).create();
		} catch (Exception e) {
			e.printStackTrace();
			throw new SmsNotSentException("Sms Not Sent.");
		}
		saveOtp(new SaveOtpDto(otp, otpRequestDto.getUserName()));
		return message;
	}

	public String generateOTP() {
		return new DecimalFormat("000000").format(new Random().nextInt(999999));
	}

	@Override
	public void saveOtp(SaveOtpDto saveOtpDto) {
		User existingUser = userRepository.findByEmail(saveOtpDto.getUserName())
				.orElseThrow(() -> new UserNotFoundException("User does not exists"));
		Otp newOtp = new Otp();
		newOtp.setOtpNumber(saveOtpDto.getOtpNumber());
		newOtp.setOtpCreationTime(LocalDateTime.now());
		newOtp.setUser(existingUser);
		otpRepository.save(newOtp);
	}

	@Override
	public void verifyOtp(OtpVerificationDto otpVerificationDto) {
		Otp otp = otpRepository.findByOtpNumber(otpVerificationDto.getOtp())
				.orElseThrow(() -> new OtpNotFoundException("Otp not found"));
		LocalDateTime nowDateTime = LocalDateTime.now();
		LocalDateTime expirationTime = otp.getOtpCreationTime().plusSeconds(30);
		if (nowDateTime.isAfter(expirationTime)) {
			throw new OtpExpiredException("Otp is expired. Resend Otp!!");
		}
		User user = otp.getUser();
		user.setActive(true);
		userRepository.save(user);
		otpRepository.delete(otp);
	}

}
