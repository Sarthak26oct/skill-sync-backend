package com.skillsyncai.dto;

import java.util.List;

public class InsightResponse {
	private int totalSkills;
	private int totalActivities;
	private int totalLearningHours;
	private int activeDays;
	private int currentStreak;
	private String recentSummary;
	private List<InsightSkillStats> topSkills;

	// Getters & Setters
	public int getTotalSkills() {
		return totalSkills;
	}

	public void setTotalSkills(int totalSkills) {
		this.totalSkills = totalSkills;
	}

	public int getTotalActivities() {
		return totalActivities;
	}

	public void setTotalActivities(int totalActivities) {
		this.totalActivities = totalActivities;
	}

	public int getTotalLearningHours() {
		return totalLearningHours;
	}

	public void setTotalLearningHours(int totalLearningHours) {
		this.totalLearningHours = totalLearningHours;
	}

	public int getActiveDays() {
		return activeDays;
	}

	public void setActiveDays(int activeDays) {
		this.activeDays = activeDays;
	}

	public int getCurrentStreak() {
		return currentStreak;
	}

	public void setCurrentStreak(int currentStreak) {
		this.currentStreak = currentStreak;
	}

	public String getRecentSummary() {
		return recentSummary;
	}

	public void setRecentSummary(String recentSummary) {
		this.recentSummary = recentSummary;
	}

	public List<InsightSkillStats> getTopSkills() {
		return topSkills;
	}

	public void setTopSkills(List<InsightSkillStats> topSkills) {
		this.topSkills = topSkills;
	}
}
