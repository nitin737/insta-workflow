package com.instaworkflow.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record GenerationRequest(
        @NotBlank String topic,
        @NotBlank String pillar,
        @NotBlank String tone,
        String targetDate,
        String templateName) {
}
