package com.example.demo.messaging.consumer;

import com.example.demo.client.ThirdPartyUserDataClient;
import com.example.demo.data.User;
import com.example.demo.messaging.message.UserCreatedMessage;
import com.example.demo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

@Component
public class UserMessageConsumer {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public static final String FAILED_TO_GET_ADDITIONAL_DATA = "FAILED_TO_GET_ADDITIONAL_DATA";

	@Autowired
	private UserService userService;

	@Autowired
	private ThirdPartyUserDataClient thirdPartyUserDataClient;

	public void handleUserCreatedMessage(UserCreatedMessage userCreatedMessage) {

		Long userId = userCreatedMessage.getUserId();
		User user = userService.getUserById(userId);

		if (user != null) {
			String additionalData;

			try {
				additionalData = thirdPartyUserDataClient.getAdditionalUserData(userId);
				logger.info("Successfully retrieved additional data [{}] for user [{}].", additionalData, userId);
			} catch (HttpClientErrorException ex) {
				additionalData = FAILED_TO_GET_ADDITIONAL_DATA;
				logger.warn("Failed to retrieve additional data for user [{}].", userId, ex);
			}

			user.setAdditionalData(additionalData);
			userService.updateUser(user);
		}

	}

}
