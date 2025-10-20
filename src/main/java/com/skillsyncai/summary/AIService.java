package com.skillsyncai.summary;

import com.skillsyncai.ApiResponse;
import com.skillsyncai.dto.SummaryRequest;
import com.skillsyncai.dto.SummaryResponse;

public interface AIService {
	ApiResponse<SummaryResponse> summarize(SummaryRequest request);

	ApiResponse<?> recommend(Long userId);

	ApiResponse<?> predict(Long userId);
}
