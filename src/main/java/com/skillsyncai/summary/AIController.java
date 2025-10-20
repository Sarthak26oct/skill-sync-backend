package com.skillsyncai.summary;

import com.skillsyncai.ApiResponse;
import com.skillsyncai.dto.SummaryRequest;
import com.skillsyncai.dto.SummaryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ai")
public class AIController {

	@Autowired
	private AIService aiService;

	@PostMapping("/summarize")
	public ResponseEntity<ApiResponse<SummaryResponse>> summarize(@RequestBody SummaryRequest request) {
		return ResponseEntity.ok(aiService.summarize(request));
	}

	@PostMapping("/recommend")
	public ResponseEntity<ApiResponse<?>> recommend(@RequestParam Long userId) {
		return ResponseEntity.ok(aiService.recommend(userId));
	}

	@PostMapping("/predict")
	public ResponseEntity<ApiResponse<?>> predict(@RequestParam Long userId) {
		return ResponseEntity.ok(aiService.predict(userId));
	}
}
