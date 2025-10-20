package com.skillsyncai.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class SkillRequest {
	private String name;
	private String description;
	private String level;
	private Integer progress;
	private LocalDate targetCompletionDate;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public Integer getProgress() {
		return progress;
	}

	public void setProgress(Integer progress) {
		this.progress = progress;
	}

	public LocalDate getTargetCompletionDate() {
		return targetCompletionDate;
	}

	public void setTargetCompletionDate(LocalDate targetCompletionDate) {
		this.targetCompletionDate = targetCompletionDate;
	}

}
