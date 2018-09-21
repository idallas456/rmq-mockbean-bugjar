package com.example.demo.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ThirdPartyUserDataClient {

	@Autowired
	private RestTemplate thirdPartyUserDataRestTemplate;


	public String getAdditionalUserData(Long userId) {
		String str = thirdPartyUserDataRestTemplate.getForObject("http://www.example.com", String.class, userId);

		if(str != null && str.length() > 5) {
			return str.substring(0, 5);
		}

		return null;
	}

}
