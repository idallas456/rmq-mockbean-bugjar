package com.example.demo;

import com.example.demo.data.User;
import com.example.demo.data.UserState;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTests {

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@Test
	public void createUpdateDeleteUserTest() {

		User user = new User();
		user.setName("Test User");
		user.setState(UserState.PENDING);

		user = userService.createUser(user);

		Assert.assertNotNull(user.getId());

		user.setState(UserState.CREATED);
		user = userService.updateUser(user);

		Assert.assertEquals(UserState.CREATED, user.getState());

		/*userService.deleteUser(user);

		user = userService.getUserById(user.getId());

		Assert.assertNull(user);*/

	}

}
