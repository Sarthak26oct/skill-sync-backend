package com.skillsyncai.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "skills")
public class Skill {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@Column(length = 1000)
	private String description;

	@Enumerated(EnumType.STRING)
	private SkillLevel level;

	private Integer progress = 0;

	private LocalDate targetCompletionDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@OneToMany(mappedBy = "skill", fetch = FetchType.LAZY, orphanRemoval = true)
	private List<Activity> activities = new ArrayList<>();

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	@PrePersist
	public void onCreate() {
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
	}

	@PreUpdate
	public void onUpdate() {
		this.updatedAt = LocalDateTime.now();
	}

	public void addActivity(Activity activity) {
		activities.add(activity);
		activity.setSkill(this);
	}

	public void removeActivity(Activity activity) {
		activities.remove(activity);
		activity.setSkill(null);
	}

	// Getters & Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public SkillLevel getLevel() {
		return level;
	}

	public void setLevel(SkillLevel level) {
		this.level = level;
	}

	public Integer getProgress() {
		return progress;
	}

	public void setProgress(Integer progress) {
		this.progress = progress;
	}

	public LocalDate getTargetCompletionDate() {
		return targetCompletionDate;
	}

	public void setTargetCompletionDate(LocalDate targetCompletionDate) {
		this.targetCompletionDate = targetCompletionDate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Activity> getActivities() {
		return activities;
	}

	public void setActivities(List<Activity> activities) {
		this.activities = activities;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
}
