package com.skillsyncai.insight;

import com.skillsyncai.ApiResponse;
import com.skillsyncai.dto.*;
import com.skillsyncai.model.User;
import java.util.List;

public interface InsightService {

	ApiResponse<InsightResponse> getOverview(User user);

	ApiResponse<List<InsightSkillStats>> getSkillInsights(User user);

	ApiResponse<WeeklyTrendResponse> getWeeklyTrend(User user);
}
