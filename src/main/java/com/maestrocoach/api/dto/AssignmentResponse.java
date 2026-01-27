package com.maestrocoach.api.dto;

import com.maestrocoach.domain.AssignmentStatus;

import java.util.UUID;

public record AssignmentResponse(UUID id, UUID studentId, UUID learningItemId, AssignmentStatus status) {
}
