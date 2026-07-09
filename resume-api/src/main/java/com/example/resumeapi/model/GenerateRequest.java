package com.example.resumeapi.model;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record GenerateRequest(
    @NotBlank(message = "templateId is mandatory") String templateId,
    @NotNull(message = "data is mandatory") JsonNode data
) {}
