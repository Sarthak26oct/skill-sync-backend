package com.skillsyncai.repository;

import com.skillsyncai.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InsightRepository extends JpaRepository<User, Long> {

	@Query("SELECT SUM(a.duration) FROM Activity a WHERE a.user = :user")
	Integer getTotalLearningTime(User user);

	@Query("SELECT COUNT(a) FROM Activity a WHERE a.user = :user")
	Integer getTotalActivityCount(User user);
}
