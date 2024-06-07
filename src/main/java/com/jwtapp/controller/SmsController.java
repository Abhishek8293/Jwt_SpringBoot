package com.jwtapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jwtapp.dto.OtpRequestDto;
import com.jwtapp.dto.OtpVerificationDto;
import com.jwtapp.response.ResponseHandler;
import com.jwtapp.service.SmsServiceImpl;
import com.twilio.rest.api.v2010.account.Message;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/sms")
@RequiredArgsConstructor
public class SmsController {

	private final SmsServiceImpl smsServiceImpl;

	@GetMapping("/testsms")
	public String smsTest() {
		return "SMS sent";
	}

	@PostMapping("/send/otp")
	public ResponseEntity<?> sendOtp(@RequestBody OtpRequestDto otpRequestDto) {
		Message response = smsServiceImpl.sendSms(otpRequestDto);
		return ResponseHandler.responseBuilder("Verification OTP Message sent successfully", HttpStatus.OK, response);
	}

	@PostMapping("/verify/otp")
	public ResponseEntity<?> verifyOtp(@RequestBody OtpVerificationDto otpVerificationDto) {
		smsServiceImpl.verifyOtp(otpVerificationDto);
		return ResponseHandler.responseBuilder("Otp verified Successfull", HttpStatus.OK, null);
	}

}
