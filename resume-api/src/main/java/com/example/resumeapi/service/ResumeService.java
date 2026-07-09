package com.example.resumeapi.service;

import com.example.resumeapi.model.EngineType;
import com.example.resumeapi.service.strategy.ResumeRendererStrategy;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class ResumeService {

    private static final Logger log = LoggerFactory.getLogger(ResumeService.class);

    private final List<ResumeRendererStrategy> strategies;
    private final ValidationService validator;

    public ResumeService(List<ResumeRendererStrategy> strategies, ValidationService validator) {
        this.strategies = strategies;
        this.validator = validator;
    }

    @Async("pdfGenerationExecutor")
    public CompletableFuture<byte[]> generatePdf(String templateId, JsonNode data, EngineType engine) {
        validator.validate(data);

        for (ResumeRendererStrategy strategy : strategies) {
            if (strategy.supports(engine)) {
                return CompletableFuture.completedFuture(strategy.render(templateId, data));
            }
        }
        throw new IllegalArgumentException("No strategy found for engine: " + engine);
    }
}
