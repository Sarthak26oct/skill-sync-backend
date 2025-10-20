package com.skillsyncai.utils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.skillsyncai.model.User;
import com.skillsyncai.repository.UserRepository;

import io.jsonwebtoken.JwtException;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	private final UserRepository userRepository;

	public JwtAuthenticationFilter(JwtUtil jwtUtil, UserRepository userRepository) {
		this.jwtUtil = jwtUtil;
		this.userRepository = userRepository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String authHeader = request.getHeader("Authorization");
		String token = null;

		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			token = authHeader.substring(7);
		}

		if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			try {
				String email = jwtUtil.getEmailFromToken(token);
				var userOpt = userRepository.findByEmail(email);

				if (userOpt.isPresent()) {
					User user = userOpt.get();

					var authorities = user.getRoles().stream().map(SimpleGrantedAuthority::new).toList();

					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user, null,
							authorities);

					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

					// Set authenticated user object in context
					SecurityContextHolder.getContext().setAuthentication(authToken);
				}
			} catch (JwtException e) {
				// Invalid or expired token → ignore and continue
			}
		}

		filterChain.doFilter(request, response);
	}
}
