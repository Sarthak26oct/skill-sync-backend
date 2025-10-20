package com.skillsyncai.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import com.skillsyncai.model.User;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

	private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
	private static final long EXPIRATION = 1000 * 60 * 60 * 2; // 2 hours

	// Generate token
	public String generateToken(User user) {
		String roles = String.join(",", user.getRoles());

		return Jwts.builder().setSubject(user.getEmail()) // Email as subject
				.claim("userId", user.getId()).claim("roles", roles).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION)).signWith(key).compact();
	}

	// Extract email (subject)
	public String getEmailFromToken(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
	}

	// Extract userId
	public Long getUserIdFromToken(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().get("userId",
				Long.class);
	}

	// Extract roles
	public List<String> getRolesFromToken(String token) {
		String roles = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().get("roles",
				String.class);

		return Arrays.stream(roles.split(",")).map(String::trim).collect(Collectors.toList());
	}
}
