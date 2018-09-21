package com.example.demo.messaging.consumer;

import com.example.demo.client.ThirdPartyUserDataClient;
import com.example.demo.data.User;
import com.example.demo.messaging.message.UserCreatedMessage;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMessageConsumer {

	@Autowired
	private UserService userService;

	@Autowired
	private ThirdPartyUserDataClient thirdPartyUserDataClient;

	public void handleUserCreatedMessage(UserCreatedMessage userCreatedMessage) {

		Long userId = userCreatedMessage.getUserId();
		User user = userService.getUserById(userId);

		if (user != null) {
			String additionalData = thirdPartyUserDataClient.getAdditionalUserData(userId);
			user.setAdditionalData(additionalData);
			userService.updateUser(user);
		}

	}

}
