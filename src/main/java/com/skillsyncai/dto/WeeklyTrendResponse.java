package com.skillsyncai.dto;

import java.time.LocalDate;
import java.util.List;

public class WeeklyTrendResponse {
	private List<String> labels;
	private List<Integer> learningHours;
	private List<LocalDate> dates;

	public List<String> getLabels() {
		return labels;
	}

	public void setLabels(List<String> labels) {
		this.labels = labels;
	}

	public List<Integer> getLearningHours() {
		return learningHours;
	}

	public void setLearningHours(List<Integer> learningHours) {
		this.learningHours = learningHours;
	}

	public List<LocalDate> getDates() {
		return dates;
	}

	public void setDates(List<LocalDate> dates) {
		this.dates = dates;
	}
}
