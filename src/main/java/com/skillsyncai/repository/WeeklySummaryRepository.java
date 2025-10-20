package com.skillsyncai.repository;

import com.skillsyncai.model.WeeklySummary;
import com.skillsyncai.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

@Repository
public interface WeeklySummaryRepository extends JpaRepository<WeeklySummary, Long> {
	Optional<WeeklySummary> findByUserAndPeriodStartAndPeriodEnd(User user, LocalDate start, LocalDate end);

	List<WeeklySummary> findAllByUser(User user);
}
