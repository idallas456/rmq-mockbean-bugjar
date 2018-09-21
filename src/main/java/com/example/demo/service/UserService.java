package com.example.demo.service;

import com.example.demo.data.User;
import com.example.demo.messaging.message.UserCreatedMessage;
import com.example.demo.repository.UserRepository;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.example.demo.config.DemoMessagingConfig.USER_EXCHANGE;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	public User createUser(User user) {
		User savedUser = userRepository.saveAndFlush(user);

		UserCreatedMessage createdMessage = new UserCreatedMessage();
		createdMessage.setUserId(savedUser.getId());
		rabbitTemplate.convertAndSend(USER_EXCHANGE , createdMessage.getClass().getCanonicalName(), createdMessage);

		return savedUser;
	}

	public User updateUser(User user) {
		return userRepository.saveAndFlush(user);
	}

	public void deleteUser(User user) {
		userRepository.deleteById(user.getId());
	}

	public User getUserById(Long id){
		Optional<User> userOptional = userRepository.findById(id);

		if(userOptional.isPresent()) {
			return userOptional.get();
		}

		return null;
	}

}
