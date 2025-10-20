package com.skillsyncai.dto;

import lombok.Builder;

@Builder
public class SummaryResponse {
	private String summaryText;

	public SummaryResponse() {
	}

	public SummaryResponse(String summaryText) {
		super();
		this.summaryText = summaryText;
	}

	public String getSummaryText() {
		return summaryText;
	}

	public void setSummaryText(String summaryText) {
		this.summaryText = summaryText;
	}

}
