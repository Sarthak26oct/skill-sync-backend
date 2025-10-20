package com.skillsyncai.skill;

import com.skillsyncai.ApiResponse;
import com.skillsyncai.dto.SkillRequest;
import com.skillsyncai.dto.SkillResponse;
import com.skillsyncai.model.User;
import java.util.List;

public interface SkillService {
	ApiResponse<SkillResponse> createSkill(SkillRequest request, User user);

	ApiResponse<List<SkillResponse>> getAllSkills(User user);

	ApiResponse<SkillResponse> getSkillById(Long id, User user);

	ApiResponse<SkillResponse> updateSkill(Long id, SkillRequest request, User user);

	ApiResponse<Void> deleteSkill(Long id, User user);
}
