package com.skillsyncai.insight;

import com.skillsyncai.ApiResponse;
import com.skillsyncai.dto.*;
import com.skillsyncai.model.Activity;
import com.skillsyncai.model.Skill;
import com.skillsyncai.model.User;
import com.skillsyncai.model.WeeklySummary;
import com.skillsyncai.repository.ActivityRepository;
import com.skillsyncai.repository.SkillRepository;
import com.skillsyncai.repository.WeeklySummaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InsightServiceImpl implements InsightService {

	@Autowired
	private SkillRepository skillRepository;

	@Autowired
	private ActivityRepository activityRepository;

	@Autowired
	private WeeklySummaryRepository weeklySummaryRepository;

	@Override
	public ApiResponse<InsightResponse> getOverview(User user) {
		List<Skill> skills = skillRepository.findByUser(user);
		List<Activity> activities = activityRepository.findAllByUser(user);

		int totalSkills = skills.size();
		int totalActivities = activities.size();
		int totalLearningHours = activities.stream().mapToInt(a -> Optional.ofNullable(a.getDuration()).orElse(0))
				.sum();
		int activeDays = (int) activities.stream().map(a -> a.getCreatedAt().toLocalDate()).distinct().count();

		int currentStreak = calculateLearningStreak(activities);

		WeeklySummary latestSummary = weeklySummaryRepository.findTopByUserOrderByCreatedAtDesc(user).orElse(null);
		String recentSummary = (latestSummary != null) ? latestSummary.getSummaryText()
				: "No AI summary generated yet.";

		Map<Long, Integer> skillHours = new HashMap<>();
		for (Activity activity : activities) {
			if (activity.getSkill() != null) {
				skillHours.merge(activity.getSkill().getId(), Optional.ofNullable(activity.getDuration()).orElse(0),
						Integer::sum);
			}
		}

		List<InsightSkillStats> topSkills = skills.stream()
				.map(s -> new InsightSkillStats(s.getName(), s.getProgress() != null ? s.getProgress() : 0,
						skillHours.getOrDefault(s.getId(), 0)))
				.sorted(Comparator.comparingInt(InsightSkillStats::getTotalHours).reversed()).limit(5)
				.collect(Collectors.toList());

		InsightResponse response = new InsightResponse();
		response.setTotalSkills(totalSkills);
		response.setTotalActivities(totalActivities);
		response.setTotalLearningHours(totalLearningHours);
		response.setActiveDays(activeDays);
		response.setCurrentStreak(currentStreak);
		response.setRecentSummary(recentSummary);
		response.setTopSkills(topSkills);

		return new ApiResponse<>(true, "Overview insights fetched successfully", HttpStatus.OK.value(), response);
	}

	@Override
	public ApiResponse<List<InsightSkillStats>> getSkillInsights(User user) {
		List<Skill> skills = skillRepository.findByUser(user);
		List<Activity> activities = activityRepository.findAllByUser(user);

		Map<Long, Integer> skillDurations = new HashMap<>();
		for (Activity activity : activities) {
			if (activity.getSkill() != null) {
				skillDurations.merge(activity.getSkill().getId(), Optional.ofNullable(activity.getDuration()).orElse(0),
						Integer::sum);
			}
		}

		List<InsightSkillStats> stats = skills.stream().map(s -> new InsightSkillStats(s.getName(),
				s.getProgress() != null ? s.getProgress() : 0, skillDurations.getOrDefault(s.getId(), 0)))
				.collect(Collectors.toList());

		return new ApiResponse<>(true, "Skill insights fetched successfully", HttpStatus.OK.value(), stats);
	}

	@Override
	public ApiResponse<WeeklyTrendResponse> getWeeklyTrend(User user) {
		List<Activity> activities = activityRepository.findAllByUser(user);

		if (activities.isEmpty()) {
			return new ApiResponse<>(true, "No activity data available for trend", HttpStatus.OK.value(),
					new WeeklyTrendResponse());
		}

		// Group by week of year
		WeekFields weekFields = WeekFields.of(Locale.getDefault());
		Map<Integer, Integer> weekHours = new TreeMap<>();

		for (Activity activity : activities) {
			LocalDate date = activity.getCreatedAt().toLocalDate();
			int week = date.get(weekFields.weekOfWeekBasedYear());
			weekHours.merge(week, Optional.ofNullable(activity.getDuration()).orElse(0), Integer::sum);
		}

		List<String> labels = new ArrayList<>();
		List<Integer> hours = new ArrayList<>();
		List<LocalDate> dates = new ArrayList<>();

		for (Map.Entry<Integer, Integer> entry : weekHours.entrySet()) {
			labels.add("Week " + entry.getKey());
			hours.add(entry.getValue());
			// Approximate date for chart
			dates.add(LocalDate.now().with(weekFields.weekOfWeekBasedYear(), entry.getKey()).with(DayOfWeek.MONDAY));
		}

		WeeklyTrendResponse trendResponse = new WeeklyTrendResponse();
		trendResponse.setLabels(labels);
		trendResponse.setLearningHours(hours);
		trendResponse.setDates(dates);

		return new ApiResponse<>(true, "Weekly trend fetched successfully", HttpStatus.OK.value(), trendResponse);
	}

	// Helper to calculate consecutive learning streak
	private int calculateLearningStreak(List<Activity> activities) {
		if (activities.isEmpty())
			return 0;

		Set<LocalDate> uniqueDays = activities.stream().map(a -> a.getCreatedAt().toLocalDate())
				.collect(Collectors.toCollection(TreeSet::new));

		int streak = 1;
		LocalDate prev = null;

		for (LocalDate date : uniqueDays) {
			if (prev != null && date.minusDays(1).equals(prev)) {
				streak++;
			} else if (prev != null && !date.minusDays(1).equals(prev)) {
				streak = 1;
			}
			prev = date;
		}
		return streak;
	}
}
