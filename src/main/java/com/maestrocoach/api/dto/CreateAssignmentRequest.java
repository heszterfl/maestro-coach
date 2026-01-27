package com.maestrocoach.api.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateAssignmentRequest(@NotNull UUID studentId, @NotNull UUID learningItemId) {
}
