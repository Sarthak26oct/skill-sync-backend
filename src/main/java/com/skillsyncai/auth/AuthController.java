package com.skillsyncai.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skillsyncai.ApiResponse;
import com.skillsyncai.dto.LoginRequest;
import com.skillsyncai.dto.SignupRequest;
import com.skillsyncai.dto.TokenRefreshRequest;
import com.skillsyncai.dto.TokenResponse;
import com.skillsyncai.model.RefreshToken;
import com.skillsyncai.utils.JwtUtil;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@Validated
public class AuthController {

	@Autowired
	private AuthService authService;

	@Autowired
	private RefreshTokenService refreshTokenService;

	@Autowired
	private JwtUtil jwtUtil;

	@PostMapping("/signup")
	public ResponseEntity<ApiResponse<TokenResponse>> signup(@Valid @RequestBody SignupRequest signupRequest) {
		try {
			ApiResponse<TokenResponse> response = authService.signup(signupRequest);
			return ResponseEntity.status(response.getStatus()).body(response);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ApiResponse<>(false, "Something went wrong: " + e.getMessage(), 500, null));
		}
	}

	@PostMapping("/login")
	public ResponseEntity<ApiResponse<TokenResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
		try {
			ApiResponse<TokenResponse> response = authService.login(loginRequest);
			return ResponseEntity.status(response.getStatus()).body(response);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ApiResponse<>(false, "Something went wrong: " + e.getMessage(), 500, null));
		}
	}

	@PostMapping("/refresh-token")
	public ResponseEntity<ApiResponse<TokenResponse>> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
		String requestToken = request.getRefreshToken();
		try {
			RefreshToken refreshToken = refreshTokenService.findByToken(requestToken)
					.orElseThrow(() -> new RuntimeException("Refresh token not found"));

			refreshTokenService.verifyExpiration(refreshToken);

			String newAccessToken = jwtUtil.generateToken(refreshToken.getUser());

			TokenResponse tokenResponse = new TokenResponse(newAccessToken, requestToken,
					refreshToken.getUser().getRoles());

			return ResponseEntity.ok(new ApiResponse<>(true, "Token refreshed successfully", 200, tokenResponse));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>(false, e.getMessage(), 401, null));
		}
	}

	@PostMapping("/logout")
	public ResponseEntity<ApiResponse<Void>> logout(@RequestHeader("Authorization") String authHeader) {
		try {
			if (authHeader == null || !authHeader.startsWith("Bearer ")) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(new ApiResponse<>(false, "Authorization header missing or invalid", 400, null));
			}

			String token = authHeader.substring(7); // remove "Bearer "
			Long userId = jwtUtil.getUserIdFromToken(token); // extract userId from JWT

			refreshTokenService.deleteByUserId(userId);

			return ResponseEntity.ok(new ApiResponse<>(true, "Logged out successfully", 200, null));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ApiResponse<>(false, "Something went wrong: " + e.getMessage(), 500, null));
		}
	}

}
