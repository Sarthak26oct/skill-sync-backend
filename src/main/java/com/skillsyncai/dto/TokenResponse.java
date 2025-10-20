package com.skillsyncai.dto;

import java.util.Set;

public class TokenResponse {

	private String accessToken;
	private String refreshToken;
	private String tokenType = "Bearer";
	private Set<String> roles;

	public TokenResponse() {
	}

	public TokenResponse(String accessToken, String refreshToken, Set<String> roles) {
		super();
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.roles = roles;
	}

	// Getters and Setters
	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

}
