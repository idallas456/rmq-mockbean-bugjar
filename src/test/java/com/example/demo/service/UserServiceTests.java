package com.example.demo.service;

import com.example.demo.client.ThirdPartyUserDataClient;
import com.example.demo.data.User;
import com.example.demo.data.UserState;
import com.example.demo.messaging.consumer.UserMessageConsumer;
import com.example.demo.repository.UserRepository;
import org.awaitility.Awaitility;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import static com.example.demo.config.DemoMessagingConfig.USER_MESSAGING_QUEUE;


@SpringBootTest
public class UserServiceTests extends AbstractTestNGSpringContextTests {

	@Autowired
	private UserService userService;

	@Autowired
	private UserMessageConsumer userMessageConsumer;

	@MockBean
	@Autowired
	private ThirdPartyUserDataClient thirdPartyUserDataClient;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RabbitAdmin rabbitAdmin;

	@Test(priority = 2)
	public void createUpdateUserTest() {

		logger.info("=============== CONSUMERS: " + rabbitAdmin.getQueueProperties(USER_MESSAGING_QUEUE).get(RabbitAdmin.QUEUE_CONSUMER_COUNT));

		String additionalData = org.apache.commons.lang3.RandomStringUtils.random(5);
		Mockito.when(thirdPartyUserDataClient.getAdditionalUserData(ArgumentMatchers.anyLong())).thenReturn(additionalData);

		User user = new User();
		user.setName("Test User");
		user.setState(UserState.PENDING);

		user = userService.createUser(user);

		Assert.assertNotNull(user.getId());

		User finalUser = user;
		Awaitility.await().until(() -> {
			User user2 = userService.getUserById(finalUser.getId());
			return finalUser != null && additionalData.equals(user2.getAdditionalData());
		});

		user.setState(UserState.CREATED);
		user = userService.updateUser(user);

		Assert.assertEquals(UserState.CREATED, user.getState());

	}

}
