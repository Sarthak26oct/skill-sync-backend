package com.skillsyncai.dto;

public class InsightSkillStats {
	private String skillName;
	private int progress;
	private int totalHours;

	public InsightSkillStats(String skillName, int progress, int totalHours) {
		this.skillName = skillName;
		this.progress = progress;
		this.totalHours = totalHours;
	}

	public String getSkillName() {
		return skillName;
	}

	public void setSkillName(String skillName) {
		this.skillName = skillName;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public int getTotalHours() {
		return totalHours;
	}

	public void setTotalHours(int totalHours) {
		this.totalHours = totalHours;
	}
}
