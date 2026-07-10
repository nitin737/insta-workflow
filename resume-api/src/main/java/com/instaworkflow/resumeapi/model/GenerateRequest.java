package com.instaworkflow.resumeapi.model;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record GenerateRequest(
    @NotBlank(message = "templateId is mandatory") String templateId,
    @NotNull(message = "data is mandatory") JsonNode data,
    EngineType engine,
    java.util.Map<String, String> theme
) {
    public GenerateRequest {
        if (engine == null) {
            engine = EngineType.HTML;
        }
        if (theme == null) {
            theme = java.util.Collections.emptyMap();
        }
    }
}
