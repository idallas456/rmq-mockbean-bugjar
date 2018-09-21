package com.example.demo.messaging.consumer;

import com.example.demo.client.ThirdPartyUserDataClient;
import com.example.demo.data.User;
import com.example.demo.data.UserState;
import com.example.demo.service.UserService;
import org.awaitility.Awaitility;

import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.web.client.HttpClientErrorException;
import org.testng.Assert;
import org.testng.annotations.Test;

import static com.example.demo.config.DemoMessagingConfig.USER_MESSAGING_QUEUE;


@SpringBootTest
public class UserMessageConsumerTest extends AbstractTestNGSpringContextTests {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserService userService;

	@Autowired
	private RabbitAdmin rabbitAdmin;

	@MockBean
	@Autowired
	private ThirdPartyUserDataClient thirdPartyUserDataClient;
	
	@Test(priority = 2)
	public void testFailureToCallThirdParty(){


		logger.info("=============== CONSUMERS: " + rabbitAdmin.getQueueProperties(USER_MESSAGING_QUEUE).get(RabbitAdmin.QUEUE_CONSUMER_COUNT));

		Mockito.when(thirdPartyUserDataClient.getAdditionalUserData(ArgumentMatchers.anyLong())).thenThrow(new HttpClientErrorException(HttpStatus.BAD_GATEWAY));

		User user = new User();
		user.setName("Test User");
		user.setState(UserState.PENDING);

		user = userService.createUser(user);

		Assert.assertNotNull(user.getId());

		User finalUser = user;
		Awaitility.await().until(() -> {
			User user2 = userService.getUserById(finalUser.getId());
			return finalUser != null && UserMessageConsumer.FAILED_TO_GET_ADDITIONAL_DATA.equals(user2.getAdditionalData());
		});

		user.setState(UserState.CREATED);
		user = userService.updateUser(user);

		Assert.assertEquals(UserState.CREATED, user.getState());

	}

}