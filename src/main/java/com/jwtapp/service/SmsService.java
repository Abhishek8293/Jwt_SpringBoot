package com.jwtapp.service;

import com.jwtapp.dto.OtpRequestDto;
import com.jwtapp.dto.OtpVerificationDto;
import com.jwtapp.dto.SaveOtpDto;
import com.twilio.rest.api.v2010.account.Message;

public interface SmsService {
	
	Message sendSms(OtpRequestDto otpRequestDto);
	
	String generateOTP();
	
	void saveOtp(SaveOtpDto saveOtpDto);
	
	void verifyOtp(OtpVerificationDto otpVerificationDto);

}
