package com.maestrocoach.api.dto;

import com.maestrocoach.domain.LearningCategory;

import java.util.UUID;

public record LearningItemResponse(UUID id, String title, LearningCategory category, String description) {
}
