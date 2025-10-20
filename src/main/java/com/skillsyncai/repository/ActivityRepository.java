package com.skillsyncai.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.skillsyncai.model.Activity;
import com.skillsyncai.model.Skill;
import com.skillsyncai.model.User;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {

	List<Activity> findAllByUserOrderByCreatedAtDesc(User user);

	List<Activity> findAllBySkillAndUserOrderByCreatedAtDesc(Skill skill, User user);

	Optional<Activity> findTopByUserOrderByCreatedAtDesc(User user);

	List<Activity> findByUserAndCreatedAtBetween(User user, LocalDateTime start, LocalDateTime end);

}
