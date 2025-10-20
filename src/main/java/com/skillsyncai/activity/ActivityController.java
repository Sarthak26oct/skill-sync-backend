package com.skillsyncai.activity;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.skillsyncai.ApiResponse;
import com.skillsyncai.dto.ActivityRequest;
import com.skillsyncai.dto.ActivityResponse;
import com.skillsyncai.model.User;
import com.skillsyncai.utils.AuthUtil;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/activities")
@Validated
public class ActivityController {

	@Autowired
	private AuthUtil authUtil;

	@Autowired
	private ActivityService activityService;

	@PostMapping
	public ApiResponse<ActivityResponse> createActivity(@Valid @RequestBody ActivityRequest activityRequest)
			throws Exception {
		User user = authUtil.getAuthenticatedUser();
		return activityService.createActivity(activityRequest, user);
	}

	@GetMapping
	public ApiResponse<List<ActivityResponse>> getAllActivities() {
		User user = authUtil.getAuthenticatedUser();
		return activityService.getAllActivities(user);
	}

	@GetMapping("/skill/{skillId}")
	public ApiResponse<List<ActivityResponse>> getActivitiesBySkill(@PathVariable Long skillId) {
		User user = authUtil.getAuthenticatedUser();
		return activityService.getActivitiesBySkill(skillId, user);
	}

	@GetMapping("/{id}")
	public ApiResponse<ActivityResponse> getActivityById(@PathVariable Long id) {
		User user = authUtil.getAuthenticatedUser();
		return activityService.getActivityById(id, user);
	}

	@PutMapping("/{id}")
	public ApiResponse<ActivityResponse> updateActivity(@PathVariable Long id,
			@Valid @RequestBody ActivityRequest request) {
		User user = authUtil.getAuthenticatedUser();
		return activityService.updateActivity(id, request, user);
	}

	@DeleteMapping("/{id}")
	public ApiResponse<Void> deleteActivity(@PathVariable Long id) {
		User user = authUtil.getAuthenticatedUser();
		return activityService.deleteActivity(id, user);
	}

	@DeleteMapping
	public ApiResponse<Void> deleteAllActivities() {
		User user = authUtil.getAuthenticatedUser();
		return activityService.deleteAllActivities(user);
	}
}
