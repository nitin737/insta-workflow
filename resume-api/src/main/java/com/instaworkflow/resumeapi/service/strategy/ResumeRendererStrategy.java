package com.instaworkflow.resumeapi.service.strategy;

import com.instaworkflow.resumeapi.model.EngineType;
import com.fasterxml.jackson.databind.JsonNode;

public interface ResumeRendererStrategy {
    boolean supports(EngineType engineType);
    byte[] render(String templateId, JsonNode data, java.util.Map<String, String> theme);
}
