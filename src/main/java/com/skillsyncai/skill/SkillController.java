package com.skillsyncai.skill;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.skillsyncai.ApiResponse;
import com.skillsyncai.dto.SkillRequest;
import com.skillsyncai.dto.SkillResponse;
import com.skillsyncai.model.User;
import com.skillsyncai.utils.AuthUtil;

import java.util.List;

@RestController
@RequestMapping("/api/v1/skills")
public class SkillController {

	@Autowired
	private SkillService skillService;

	@Autowired
	private AuthUtil authUtil;

	@PostMapping
	public ApiResponse<SkillResponse> createSkill(@RequestBody SkillRequest request) {
		User user = authUtil.getAuthenticatedUser();
		return skillService.createSkill(request, user);
	}

	@GetMapping
	public ApiResponse<List<SkillResponse>> getAllSkills() {
		User user = authUtil.getAuthenticatedUser();
		return skillService.getAllSkills(user);
	}

	@GetMapping("/{id}")
	public ApiResponse<SkillResponse> getSkillById(@PathVariable Long id) {
		User user = authUtil.getAuthenticatedUser();
		return skillService.getSkillById(id, user);
	}

	@PutMapping("/{id}")
	public ApiResponse<SkillResponse> updateSkill(@PathVariable Long id, @RequestBody SkillRequest request) {
		User user = authUtil.getAuthenticatedUser();
		return skillService.updateSkill(id, request, user);
	}

	@DeleteMapping("/{id}")
	public ApiResponse<Void> deleteSkill(@PathVariable Long id) {
		User user = authUtil.getAuthenticatedUser();
		return skillService.deleteSkill(id, user);
	}
}
