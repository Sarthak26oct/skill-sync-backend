package com.skillsyncai.activity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.skillsyncai.ApiResponse;
import com.skillsyncai.dto.ActivityRequest;
import com.skillsyncai.dto.ActivityResponse;
import com.skillsyncai.model.Activity;
import com.skillsyncai.model.Skill;
import com.skillsyncai.model.User;
import com.skillsyncai.repository.ActivityRepository;
import com.skillsyncai.repository.SkillRepository;

@Service
public class ActivityServiceImpl implements ActivityService {

	@Autowired
	private ActivityRepository activityRepository;

	@Autowired
	private SkillRepository skillRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public ApiResponse<ActivityResponse> createActivity(ActivityRequest request, User user) {
		Skill skill = null;

		if (request.getSkillId() != null) {
			skill = skillRepository.findById(request.getSkillId())
					.orElseThrow(() -> new RuntimeException("Skill not found"));

			if (!skill.getUser().getId().equals(user.getId())) {
				return new ApiResponse<>(false, "Unauthorized access", HttpStatus.FORBIDDEN.value());
			}
		}

		Activity activity = new Activity();
		activity.setTitle(request.getTitle());
		activity.setType(request.getType());
		activity.setSource(request.getSource());
		activity.setDuration(request.getDuration());
		activity.setUser(user);
		activity.setSkill(skill);
		activity.setPayload(request.getPayload());

		Activity saved = activityRepository.save(activity);

		ActivityResponse response = modelMapper.map(saved, ActivityResponse.class);
		return new ApiResponse<>(true, "Activity created successfully", HttpStatus.CREATED.value(), response);
	}

	@Override
	public ApiResponse<List<ActivityResponse>> getAllActivities(User user) {
		List<Activity> activities = activityRepository.findAllByUserOrderByCreatedAtDesc(user);

		List<ActivityResponse> responses = activities.stream().map(a -> modelMapper.map(a, ActivityResponse.class))
				.collect(Collectors.toList());
		return new ApiResponse<>(true, "Activities fetched successfully", HttpStatus.OK.value(), responses);
	}

	@Override
	public ApiResponse<List<ActivityResponse>> getActivitiesBySkill(Long skillId, User user) {
		Skill skill = skillRepository.findById(skillId).orElseThrow(() -> new RuntimeException("Skill not found"));
		List<Activity> activities = activityRepository.findAllBySkillAndUserOrderByCreatedAtDesc(skill, user);
		List<ActivityResponse> responses = activities.stream().map(a -> modelMapper.map(a, ActivityResponse.class))
				.collect(Collectors.toList());
		return new ApiResponse<>(true, "Activities fetched successfully", HttpStatus.OK.value(), responses);
	}

	@Override
	public ApiResponse<ActivityResponse> getActivityById(Long id, User user) {
		Optional<Activity> optionalActivity = activityRepository.findById(id);

		if (optionalActivity.isEmpty()) {
			return new ApiResponse<>(false, "Activity not found", HttpStatus.NOT_FOUND.value());
		}

		Activity activity = optionalActivity.get();

		if (!activity.getUser().getId().equals(user.getId())) {
			return new ApiResponse<>(false, "You are not authorized to access this activity",
					HttpStatus.FORBIDDEN.value());
		}

		ActivityResponse response = modelMapper.map(activity, ActivityResponse.class);
		return new ApiResponse<>(true, "Activity fetched successfully", HttpStatus.OK.value(), response);
	}

	@Override
	public ApiResponse<ActivityResponse> updateActivity(Long id, ActivityRequest request, User user) {
		Optional<Activity> optionalActivity = activityRepository.findById(id);

		if (optionalActivity.isEmpty()) {
			return new ApiResponse<>(false, "Activity not found", HttpStatus.NOT_FOUND.value());
		}

		Activity activity = optionalActivity.get();

		if (!activity.getUser().getId().equals(user.getId())) {
			return new ApiResponse<>(false, "You are not authorized to update this activity",
					HttpStatus.FORBIDDEN.value());
		}

		activity.setTitle(request.getTitle());
		activity.setType(request.getType());
		activity.setSource(request.getSource());
		activity.setDuration(request.getDuration());
		activity.setPayload(request.getPayload());

		Activity saved = activityRepository.save(activity);

		ActivityResponse response = modelMapper.map(saved, ActivityResponse.class);
		return new ApiResponse<>(true, "Activity updated successfully", HttpStatus.CREATED.value(), response);
	}

	@Override
	public ApiResponse<Void> deleteActivity(Long id, User user) {
		Optional<Activity> optionalActivity = activityRepository.findById(id);

		if (optionalActivity.isEmpty()) {
			return new ApiResponse<>(false, "Activity not found", HttpStatus.NOT_FOUND.value());
		}

		Activity activity = optionalActivity.get();

		if (!activity.getUser().getId().equals(user.getId())) {
			return new ApiResponse<>(false, "You are not authorized to delete this activity",
					HttpStatus.FORBIDDEN.value());
		}

		activityRepository.delete(activity);

		return new ApiResponse<>(true, "Activity deleted successfully", HttpStatus.OK.value());
	}

	@Override
	public ApiResponse<Void> deleteAllActivities(User user) {
		List<Activity> activities = activityRepository.findAllByUserOrderByCreatedAtDesc(user);

		if (activities.isEmpty()) {
			return new ApiResponse<>(true, "No activities found for this user", HttpStatus.OK.value(), null);
		}

		activityRepository.deleteAll(activities);

		return new ApiResponse<>(true, "All activities deleted successfully", HttpStatus.OK.value(), null);
	}

	@Override
	public List<ActivityResponse> getActivitiesBetween(User user, LocalDate startDate, LocalDate endDate) {
		List<Activity> activities = activityRepository.findByUserAndCreatedAtBetween(user, startDate.atStartOfDay(),
				endDate.atTime(23, 59, 59));

		return activities.stream().map(a -> modelMapper.map(a, ActivityResponse.class)).collect(Collectors.toList());
	}
}
