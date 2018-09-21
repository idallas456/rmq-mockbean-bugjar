package com.example.demo;

import com.example.demo.client.ThirdPartyUserDataClient;
import com.example.demo.data.User;
import com.example.demo.data.UserState;
import com.example.demo.messaging.consumer.UserMessageConsumer;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import org.awaitility.Awaitility;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.Callable;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTests {

	@Autowired
	private UserService userService;

	@Autowired
	private UserMessageConsumer userMessageConsumer;

	@MockBean
	private ThirdPartyUserDataClient thirdPartyUserDataClient;

	@Autowired
	private UserRepository userRepository;

	@Test
	public void createUpdateDeleteUserTest() {

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

		/*userService.deleteUser(user);

		user = userService.getUserById(user.getId());

		Assert.assertNull(user);*/

	}

}
