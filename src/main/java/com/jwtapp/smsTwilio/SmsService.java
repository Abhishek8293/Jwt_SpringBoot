package com.jwtapp.smsTwilio;

import java.text.DecimalFormat;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SmsService {

	private final TwilioConfig twilioConfig;

	public Message sendSms(OtpRequestDto otpRequestDto) {
		Message message = null;
		try {
			PhoneNumber to = new PhoneNumber(otpRequestDto.getPhoneNumber());
			PhoneNumber from = new PhoneNumber(twilioConfig.getPhoneNumber());
			String otpMessage = "Dear " + otpRequestDto.getUserName() + ", Your verification OTP : " + generateOTP()
					+ " for [COMPNAY-NAME] Valid for next 2 minutes. Please do not share the OTP.";
			message = Message.creator(to, from, otpMessage).create();
		} catch (Exception e) {
			e.printStackTrace();
			throw new SmsNotSentException("Sms Not Sent.");
		}
		return message;
	}

	private String generateOTP() {
		return new DecimalFormat("000000").format(new Random().nextInt(999999));
	}

}
