package com.skillsyncai;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * Handles @Valid validation errors from DTOs
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse<Void>> handleValidationExceptions(MethodArgumentNotValidException ex) {

		List<String> errors = ex.getBindingResult().getAllErrors().stream().map(error -> error.getDefaultMessage())
				.collect(Collectors.toList());

		String errorMessage = String.join("; ", errors);

		ApiResponse<Void> response = new ApiResponse<>(false, errorMessage, HttpStatus.BAD_REQUEST.value(), null);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	/**
	 * Handles invalid JSON or Enum conversion (like ActivityType)
	 */
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ApiResponse<Void>> handleInvalidFormat(HttpMessageNotReadableException ex) {
		String message = "Invalid request format. Please check the provided data.";

		// Detect Enum conversion failure
		if (ex.getLocalizedMessage().contains("ActivityType")) {
			message = "Invalid activity type. Allowed values: VIDEO, COURSE, COMMIT, MANUAL";
		}
		if (ex.getLocalizedMessage().contains("SkillLevel")) {
			message = "Invalid skill level. Allowed values: BEGINNER, INTERMEDIATE, ADVANCED, EXPERT";
		}

		ApiResponse<Void> response = new ApiResponse<>(false, message, HttpStatus.BAD_REQUEST.value(), null);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	/**
	 * Handles all other unexpected exceptions
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<Void>> handleGeneralException(Exception ex) {
		ApiResponse<Void> response = new ApiResponse<>(false, ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(),
				null);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}
}
