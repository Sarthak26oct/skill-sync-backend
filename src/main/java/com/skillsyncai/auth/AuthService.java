package com.skillsyncai.auth;

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.skillsyncai.ApiResponse;
import com.skillsyncai.dto.LoginRequest;
import com.skillsyncai.dto.SignupRequest;
import com.skillsyncai.dto.TokenResponse;
import com.skillsyncai.model.RefreshToken;
import com.skillsyncai.model.User;
import com.skillsyncai.repository.UserRepository;
import com.skillsyncai.utils.JwtUtil;

@Service
public class AuthService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private RefreshTokenService refreshTokenService;

	public ApiResponse<TokenResponse> signup(SignupRequest signupRequest) {
		if (userRepository.findByEmail(signupRequest.getEmail()).isPresent()) {
			return new ApiResponse<>(false, "Email Already Exist", 400, null);
		}

		User user = new User();
		user.setName(signupRequest.getName());
		user.setEmail(signupRequest.getEmail());
		user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));

		Set<String> roles = signupRequest.getRoles();
		if (roles == null || roles.isEmpty()) {
			roles = Set.of("ROLE_USER");
		}
		user.setRoles(roles);

		userRepository.save(user);

		// Generate access token
		String accessToken = jwtUtil.generateToken(user);

		// Generate refresh token
		RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

		TokenResponse tokenResponse = new TokenResponse(accessToken, refreshToken.getToken(), user.getRoles());

		return new ApiResponse<>(true, "User registered successfully", 201, tokenResponse);
	}

	public ApiResponse<TokenResponse> login(LoginRequest loginRequest) {
		Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());

		if (!userOptional.isPresent()) {
			return new ApiResponse<>(false, "User not found", 404, null);
		}

		User user = userOptional.get();

		if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
			return new ApiResponse<>(false, "Invalid credentials", 401, null);
		}

		// Generate JWT access token
		String accessToken = jwtUtil.generateToken(user);

		// Generate refresh token
		RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

		TokenResponse tokenResponse = new TokenResponse(accessToken, refreshToken.getToken(), user.getRoles());

		return new ApiResponse<>(true, "Login successful", 200, tokenResponse);
	}

}
