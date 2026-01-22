package com.maestrocoach.api.dto;

import java.util.UUID;

public record TeacherResponse(UUID id, String fullName, String email) {
}
