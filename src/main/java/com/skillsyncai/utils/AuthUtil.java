package com.skillsyncai.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import com.skillsyncai.model.User;
import com.skillsyncai.repository.UserRepository;

@Component
public class AuthUtil {

	private final UserRepository userRepository;

	public AuthUtil(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public User getAuthenticatedUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth == null || auth.getPrincipal() == null) {
			throw new RuntimeException("No authentication context found");
		}

		Object principal = auth.getPrincipal();

		// If principal is full User entity
		if (principal instanceof User user) {
			return user;
		}

		// If only email stored as principal
		if (principal instanceof String email) {
			return userRepository.findByEmail(email)
					.orElseThrow(() -> new RuntimeException("User not found for email: " + email));
		}

		throw new RuntimeException("Unknown principal type: " + principal.getClass());
	}
}
