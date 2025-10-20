package com.skillsyncai.auth;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skillsyncai.model.RefreshToken;
import com.skillsyncai.model.User;
import com.skillsyncai.repository.RefreshTokenRepository;

@Service
public class RefreshTokenService {

	@Autowired
	private RefreshTokenRepository refreshTokenRepository;

	private final long refreshTokenDurationMs = 1000 * 60 * 60 * 24 * 7; // 7 days

	public RefreshToken createRefreshToken(User user) {
		Optional<RefreshToken> existingToken = refreshTokenRepository.findByUserId(user.getId());

		if (existingToken.isPresent()) {
			try {
				return verifyExpiration(existingToken.get()); // reuse if not expired
			} catch (RuntimeException e) {
				refreshTokenRepository.delete(existingToken.get());
			}
		}

		RefreshToken refreshToken = new RefreshToken();
		refreshToken.setUser(user);
		refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
		refreshToken.setToken(UUID.randomUUID().toString());

		return refreshTokenRepository.save(refreshToken);
	}

	public Optional<RefreshToken> findByToken(String token) {
		return refreshTokenRepository.findByToken(token);
	}

	public RefreshToken verifyExpiration(RefreshToken token) {
		if (token.getExpiryDate().isBefore(Instant.now())) {
			refreshTokenRepository.delete(token);
			throw new RuntimeException("Refresh token has expired. Please login again.");
		}
		return token;
	}

	@Transactional
	public void deleteByUserId(Long userId) {
		refreshTokenRepository.deleteByUserId(userId);
	}
}
