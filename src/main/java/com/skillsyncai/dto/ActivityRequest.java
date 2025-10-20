package com.skillsyncai.dto;

import com.skillsyncai.model.ActivityType;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ActivityRequest {

	@NotBlank(message = "Title is required")
	private String title;

	@NotNull(message = "Activity type is required")
	private ActivityType type;

	@NotBlank(message = "Source is required")
	private String source;

	@Min(value = 1, message = "Duration must be at least 1 minute")
	private Integer duration;

	private Long skillId;

	private String payload;

	// Getters & Setters
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public ActivityType getType() {
		return type;
	}

	public void setType(ActivityType type) {
		this.type = type;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Long getSkillId() {
		return skillId;
	}

	public void setSkillId(Long skillId) {
		this.skillId = skillId;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

}
