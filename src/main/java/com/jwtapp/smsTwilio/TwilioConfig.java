package com.jwtapp.smsTwilio;

import org.springframework.stereotype.Component;

import com.jwtapp.configdetails.TwilioConfigProperties;
import com.twilio.Twilio;
import jakarta.annotation.PostConstruct;
import lombok.Data;

@Component
@Data
public class TwilioConfig {

	private String accountSid = TwilioConfigProperties.ACCOUNT_SID;
	private String authToken = TwilioConfigProperties.AUTH_TOKEN;
	private String phoneNumber = TwilioConfigProperties.PHONE_NUMBER;

	@PostConstruct
	public void setup() {
		Twilio.init(accountSid, authToken);
	}

}
