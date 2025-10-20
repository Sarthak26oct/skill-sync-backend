package com.skillsyncai.dto;

import com.skillsyncai.model.ActivityType;
import java.time.LocalDateTime;

public class ActivityResponse {

	private Long id;
	private String title;
	private ActivityType type;
	private String source;
	private Integer duration;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	private SkillResponse skill;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public SkillResponse getSkill() {
		return skill;
	}

	public void setSkill(SkillResponse skill) {
		this.skill = skill;
	}
}
