package com.example.demo.messaging.consumer;

import com.example.demo.client.ThirdPartyUserDataClient;
import com.example.demo.data.User;
import com.example.demo.data.UserState;
import com.example.demo.service.UserService;
import org.awaitility.Awaitility;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserMessageConsumerTest {

	@Autowired
	private UserService userService;

	@MockBean
	private ThirdPartyUserDataClient thirdPartyUserDataClient;


	@Test
	public void testFailureToCallThirdParty(){

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