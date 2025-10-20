package com.skillsyncai.skill;

import com.skillsyncai.ApiResponse;
import com.skillsyncai.dto.SkillRequest;
import com.skillsyncai.dto.SkillResponse;
import com.skillsyncai.model.Skill;
import com.skillsyncai.model.SkillLevel;
import com.skillsyncai.model.User;
import com.skillsyncai.repository.ActivityRepository;
import com.skillsyncai.repository.SkillRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SkillServiceImpl implements SkillService {

	@Autowired
	private SkillRepository skillRepository;

	@Autowired
	private ActivityRepository activityRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public ApiResponse<SkillResponse> createSkill(SkillRequest request, User user) {
		Skill skill = modelMapper.map(request, Skill.class);
		skill.setUser(user);
		skill.setLevel(SkillLevel.valueOf(request.getLevel().toUpperCase()));

		Skill saved = skillRepository.save(skill);
		SkillResponse response = modelMapper.map(saved, SkillResponse.class);

		return new ApiResponse<>(true, "Skill created successfully", HttpStatus.CREATED.value(), response);
	}

	@Override
	public ApiResponse<List<SkillResponse>> getAllSkills(User user) {
		List<SkillResponse> skills = skillRepository.findByUser(user).stream()
				.map(s -> modelMapper.map(s, SkillResponse.class)).collect(Collectors.toList());

		return new ApiResponse<>(true, "Skills fetched successfully", HttpStatus.OK.value(), skills);
	}

	@Override
	public ApiResponse<SkillResponse> getSkillById(Long id, User user) {
		Optional<Skill> optionalSkill = skillRepository.findById(id);

		if (optionalSkill.isEmpty()) {
			return new ApiResponse<>(false, "Skill not found", HttpStatus.NOT_FOUND.value());
		}

		Skill skill = optionalSkill.get();

		if (!skill.getUser().getId().equals(user.getId())) {
			return new ApiResponse<>(false, "You are not authorized to access this skill",
					HttpStatus.FORBIDDEN.value());
		}

		SkillResponse response = modelMapper.map(skill, SkillResponse.class);
		return new ApiResponse<>(true, "Skill fetched successfully", HttpStatus.OK.value(), response);
	}

	@Override
	public ApiResponse<SkillResponse> updateSkill(Long id, SkillRequest request, User user) {
		Optional<Skill> optionalSkill = skillRepository.findById(id);

		if (optionalSkill.isEmpty()) {
			return new ApiResponse<>(false, "Skill not found", HttpStatus.NOT_FOUND.value());
		}

		Skill skill = optionalSkill.get();

		if (!skill.getUser().getId().equals(user.getId())) {
			return new ApiResponse<>(false, "You are not authorized to update this skill",
					HttpStatus.FORBIDDEN.value());
		}

		skill.setName(request.getName());
		skill.setDescription(request.getDescription());
		skill.setLevel(SkillLevel.valueOf(request.getLevel().toUpperCase()));
		skill.setProgress(request.getProgress());
		skill.setTargetCompletionDate(request.getTargetCompletionDate());

		Skill updated = skillRepository.save(skill);
		SkillResponse response = modelMapper.map(updated, SkillResponse.class);

		return new ApiResponse<>(true, "Skill updated successfully", HttpStatus.OK.value(), response);
	}

	@Override
	public ApiResponse<Void> deleteSkill(Long id, User user) {
		Optional<Skill> optionalSkill = skillRepository.findById(id);

		if (optionalSkill.isEmpty()) {
			return new ApiResponse<>(false, "Skill not found", HttpStatus.NOT_FOUND.value());
		}

		Skill skill = optionalSkill.get();

		if (!skill.getUser().getId().equals(user.getId())) {
			return new ApiResponse<>(false, "You are not authorized to delete this skill",
					HttpStatus.FORBIDDEN.value());
		}

		activityRepository.deleteAll(skill.getActivities());

		skillRepository.delete(skill);
		return new ApiResponse<>(true, "Skill and related activities deleted successfully", HttpStatus.OK.value());
	}
}
