package com.skillsyncai.summary;

import com.skillsyncai.ApiResponse;
import com.skillsyncai.dto.SummaryRequest;
import com.skillsyncai.dto.SummaryResponse;
import com.skillsyncai.model.User;
import com.skillsyncai.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class AIScheduler {

	private static final Logger log = LoggerFactory.getLogger(AIScheduler.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AIService aiService;

	/**
	 * Runs every Sunday at 10:00 AM Asia/Kolkata time and generates weekly
	 * summaries for all users. Adjust cron or timezone if your server uses a
	 * different zone.
	 *
	 * Cron format: second minute hour day-of-month month day-of-week Here: 0 0 10 *
	 * * SUN -> every Sunday 10:00:00
	 */
	@Scheduled(cron = "0 0 10 * * SUN", zone = "Asia/Kolkata")
	public void generateWeeklySummariesForAllUsers() {
		try {
			log.info("AIScheduler: Starting weekly summaries job");

			LocalDate end = LocalDate.now(); // today
			LocalDate start = end.minusDays(7); // last 7 days

			List<User> users = userRepository.findAll();
			log.info("AIScheduler: Found {} users. Generating summaries from {} to {}", users.size(), start, end);

			for (User user : users) {
				try {
					SummaryRequest req = new SummaryRequest();
					req.setUserId(user.getId());
					req.setPeriodStart(start);
					req.setPeriodEnd(end);

					ApiResponse<SummaryResponse> resp = aiService.summarize(req);
					if (resp != null && resp.isSuccess()) {
						log.info("AIScheduler: Summary created for userId={} msg={}", user.getId(), resp.getMessage());
					} else if (resp != null) {
						log.warn("AIScheduler: Summary generation for userId={} returned failure: {}", user.getId(),
								resp.getMessage());
					} else {
						log.warn("AIScheduler: summarize() returned null for userId={}", user.getId());
					}

				} catch (Exception exUser) {
					log.error("AIScheduler: Error generating summary for userId={}: {}", user.getId(),
							exUser.getMessage(), exUser);
				}
			}

			log.info("AIScheduler: Weekly summaries job finished");
		} catch (Exception ex) {
			log.error("AIScheduler: Unexpected error running weekly summaries job: {}", ex.getMessage(), ex);
		}
	}
}
