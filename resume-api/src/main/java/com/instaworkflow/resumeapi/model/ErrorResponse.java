package com.instaworkflow.resumeapi.model;

public record ErrorResponse(
    String timestamp,
    int status,
    String error,
    String message,
    String path
) {}
