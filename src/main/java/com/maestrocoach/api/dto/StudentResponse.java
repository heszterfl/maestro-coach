package com.maestrocoach.api.dto;

import java.util.UUID;

public record StudentResponse(UUID id, String fullName, String email, String instrument, UUID teacherId) {
}
