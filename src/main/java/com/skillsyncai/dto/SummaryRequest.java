package com.skillsyncai.dto;

import java.time.LocalDate;
import lombok.*;

@Builder
public class SummaryRequest {
	private Long userId;
	private LocalDate periodStart;
	private LocalDate periodEnd;

	public SummaryRequest() {

	}

	public SummaryRequest(Long userId, LocalDate periodStart, LocalDate periodEnd) {
		super();
		this.userId = userId;
		this.periodStart = periodStart;
		this.periodEnd = periodEnd;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public LocalDate getPeriodStart() {
		return periodStart;
	}

	public void setPeriodStart(LocalDate periodStart) {
		this.periodStart = periodStart;
	}

	public LocalDate getPeriodEnd() {
		return periodEnd;
	}

	public void setPeriodEnd(LocalDate periodEnd) {
		this.periodEnd = periodEnd;
	}

}
