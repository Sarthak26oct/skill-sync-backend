package com.skillsyncai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SkillSyncAiBackendApplication {
	public static void main(String[] args) {
		SpringApplication.run(SkillSyncAiBackendApplication.class, args);
	}
}
