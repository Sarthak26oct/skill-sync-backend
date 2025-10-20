package com.skillsyncai.repository;

import com.skillsyncai.model.Skill;
import com.skillsyncai.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SkillRepository extends JpaRepository<Skill, Long> {
	List<Skill> findByUser(User user);
}
