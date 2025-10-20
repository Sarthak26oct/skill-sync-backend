package com.skillsyncai.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.skillsyncai.model.User;
import com.skillsyncai.repository.UserRepository;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@GetMapping
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	@PostMapping
	public User createUser(@RequestBody User user) {
		return userRepository.save(user);
	}
}
