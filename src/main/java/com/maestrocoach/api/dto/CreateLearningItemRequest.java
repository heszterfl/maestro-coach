package com.maestrocoach.api.dto;

import com.maestrocoach.domain.LearningCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateLearningItemRequest(@NotBlank String title, @NotNull LearningCategory category, String description) {
}
