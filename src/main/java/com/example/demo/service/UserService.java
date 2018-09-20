package com.example.demo.service;

import com.example.demo.data.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public User createUser(User user) {
		return userRepository.saveAndFlush(user);
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
