package com.maestrocoach.api.error;

public record ApiErrorResponse(String message, int status, String path) {
}
