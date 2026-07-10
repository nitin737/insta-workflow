package com.instaworkflow.resumeapi.model;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;

public record AsyncGenerateRequest(
    @NotBlank(message = "templateId is mandatory") String templateId,
    @NotNull(message = "data is mandatory") JsonNode data,
    EngineType engine,
    Map<String, String> theme,
    @NotBlank(message = "webhookUrl is mandatory") String webhookUrl
) {
    public AsyncGenerateRequest {
        if (engine == null) {
            engine = EngineType.HTML;
        }
        if (theme == null) {
            theme = java.util.Collections.emptyMap();
        }
    }
}
