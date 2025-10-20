package com.skillsyncai.activity;

import com.skillsyncai.ApiResponse;
import com.skillsyncai.dto.ActivityRequest;
import com.skillsyncai.dto.ActivityResponse;
import com.skillsyncai.model.User;

import java.time.LocalDate;
import java.util.List;

public interface ActivityService {

	ApiResponse<ActivityResponse> createActivity(ActivityRequest request, User user) throws Exception;

	ApiResponse<List<ActivityResponse>> getAllActivities(User user);

	ApiResponse<List<ActivityResponse>> getActivitiesBySkill(Long skillId, User user);

	ApiResponse<ActivityResponse> getActivityById(Long id, User user);

	ApiResponse<ActivityResponse> updateActivity(Long id, ActivityRequest request, User user);

	ApiResponse<Void> deleteActivity(Long id, User user);

	ApiResponse<Void> deleteAllActivities(User user);

	List<ActivityResponse> getActivitiesBetween(User user, LocalDate startDate, LocalDate endDate);

}
