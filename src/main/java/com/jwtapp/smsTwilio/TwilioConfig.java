package com.jwtapp.smsTwilio;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.twilio.Twilio;
import jakarta.annotation.PostConstruct;
import lombok.Data;

@Component
@Data
public class TwilioConfig {
	
	@Value("${twilio.AccountSid}")
	private String accountSid;
	@Value("${twilio.AuthToken}")
	private String authToken;
	@Value("${twilio.PhoneNumber}")
	private String phoneNumber;

	@PostConstruct
	public void setup() {
		Twilio.init(accountSid, authToken);
	}

}
