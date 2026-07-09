package com.example.resumeapi.service.strategy;

import com.example.resumeapi.model.EngineType;
import com.fasterxml.jackson.databind.JsonNode;

public interface ResumeRendererStrategy {
    boolean supports(EngineType engineType);
    byte[] render(String templateId, JsonNode data);
}
