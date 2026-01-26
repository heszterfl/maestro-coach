package com.maestrocoach.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateStudentRequest(@NotBlank String fullName, @NotBlank @Email String email, @NotBlank String instrument) {
}
