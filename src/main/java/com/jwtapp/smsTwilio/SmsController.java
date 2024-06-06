package com.jwtapp.smsTwilio;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jwtapp.response.ResponseHandler;
import com.twilio.rest.api.v2010.account.Message;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/sms")
@RequiredArgsConstructor
public class SmsController {
	
	private final SmsService smsService;
	
	@GetMapping("/testsms")
	public String smsTest() {
		return "SMS sent";
	}
	
	@PostMapping
	public ResponseEntity<?> sendOtp(@RequestBody OtpRequestDto otpRequestDto){
		Message response = smsService.sendSms(otpRequestDto);
		return ResponseHandler.responseBuilder("Verification OTP Message sent successfully", HttpStatus.OK, response);
	}

}
