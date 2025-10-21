package com.skillsyncai.insight;

import com.skillsyncai.ApiResponse;
import com.skillsyncai.dto.*;
import com.skillsyncai.model.User;
import com.skillsyncai.utils.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/insights")
public class InsightController {

	@Autowired
	private InsightService insightService;

	@Autowired
	private AuthUtil authUtil;

	@GetMapping("/overview")
	public ApiResponse<InsightResponse> getOverview() {
		User user = authUtil.getAuthenticatedUser();
		return insightService.getOverview(user);
	}

	@GetMapping("/skills")
	public ApiResponse<List<InsightSkillStats>> getSkillInsights() {
		User user = authUtil.getAuthenticatedUser();
		return insightService.getSkillInsights(user);
	}

	@GetMapping("/weekly")
	public ApiResponse<WeeklyTrendResponse> getWeeklyTrend() {
		User user = authUtil.getAuthenticatedUser();
		return insightService.getWeeklyTrend(user);
	}
}
