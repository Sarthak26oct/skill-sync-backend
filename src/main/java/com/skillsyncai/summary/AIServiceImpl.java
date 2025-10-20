package com.skillsyncai.summary;

import com.skillsyncai.ApiResponse;
import com.skillsyncai.activity.ActivityService;
import com.skillsyncai.dto.ActivityResponse;
import com.skillsyncai.dto.SummaryRequest;
import com.skillsyncai.dto.SummaryResponse;
import com.skillsyncai.model.User;
import com.skillsyncai.model.WeeklySummary;
import com.skillsyncai.repository.UserRepository;
import com.skillsyncai.repository.WeeklySummaryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AIServiceImpl implements AIService {

	@Autowired
	private WeeklySummaryRepository weeklySummaryRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ActivityService activityService;

	@Override
	public ApiResponse<SummaryResponse> summarize(SummaryRequest request) {
		try {
			Optional<User> userOpt = userRepository.findById(request.getUserId());
			if (userOpt.isEmpty()) {
				return new ApiResponse<>(false, "User not found", HttpStatus.NOT_FOUND.value(), null);
			}

			User user = userOpt.get();

			// Fetch user activities within the date range
			List<ActivityResponse> activities = activityService.getActivitiesBetween(user, request.getPeriodStart(),
					request.getPeriodEnd());

			// Generate a mock AI summary
			String summaryText = generateMockSummary(activities, request.getPeriodStart(), request.getPeriodEnd());

			// Save to weekly_summaries table
			WeeklySummary summary = new WeeklySummary();
			summary.setUser(user);
			summary.setPeriodStart(request.getPeriodStart());
			summary.setPeriodEnd(request.getPeriodEnd());
			summary.setSummaryText(summaryText);
			weeklySummaryRepository.save(summary);

			// Prepare response
			SummaryResponse response = new SummaryResponse();
			response.setSummaryText(summaryText);

			return new ApiResponse<>(true, "Weekly summary generated successfully", HttpStatus.OK.value(), response);

		} catch (Exception e) {
			return new ApiResponse<>(false, "Error generating summary: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
		}
	}

	private String generateMockSummary(List<ActivityResponse> activities, LocalDate start, LocalDate end) {
		if (activities == null || activities.isEmpty()) {
			return "No activities recorded between " + start + " and " + end + ". Keep up your learning next week!";
		}

		// Extract readable titles or skill names from the nested SkillResponse
		String activityTitles = activities.stream().map(a -> {
			if (a.getTitle() != null && !a.getTitle().isBlank()) {
				return a.getTitle();
			} else if (a.getSkill() != null && a.getSkill().getName() != null && !a.getSkill().getName().isBlank()) {
				return a.getSkill().getName();
			} else {
				return "Unnamed Activity";
			}
		}).collect(Collectors.joining(", "));

		// Calculate total learning hours (use duration field)
		double totalHours = activities.stream().filter(a -> a.getDuration() != null)
				.mapToDouble(ActivityResponse::getDuration).sum();

		return "Between " + start + " and " + end + ", you completed activities in: " + activityTitles
				+ ". You spent approximately " + totalHours + " hours learning. Excellent progress this week!";
	}

	@Override
	public ApiResponse<?> recommend(Long userId) {
		try {
			Optional<User> userOpt = userRepository.findById(userId);
			if (userOpt.isEmpty()) {
				return new ApiResponse<>(false, "User not found", HttpStatus.NOT_FOUND.value(), null);
			}

			String mockRecommendation = "Based on your recent activity, you should focus on 'Advanced React Patterns' next.";

			return new ApiResponse<>(true, "Recommendation generated successfully", HttpStatus.OK.value(),
					mockRecommendation);

		} catch (Exception e) {
			return new ApiResponse<>(false, "Error generating recommendation: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
		}
	}

	@Override
	public ApiResponse<?> predict(Long userId) {
		try {
			Optional<User> userOpt = userRepository.findById(userId);
			if (userOpt.isEmpty()) {
				return new ApiResponse<>(false, "User not found", HttpStatus.NOT_FOUND.value(), null);
			}

			String mockPrediction = "Predicted proficiency: 72% overall, strongest in Backend Development.";

			return new ApiResponse<>(true, "Prediction generated successfully", HttpStatus.OK.value(), mockPrediction);

		} catch (Exception e) {
			return new ApiResponse<>(false, "Error generating prediction: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
		}
	}
}
