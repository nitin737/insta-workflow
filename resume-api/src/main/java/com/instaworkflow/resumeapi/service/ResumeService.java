package com.instaworkflow.resumeapi.service;

import com.instaworkflow.resumeapi.model.EngineType;
import com.instaworkflow.resumeapi.service.strategy.ResumeRendererStrategy;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import com.instaworkflow.resumeapi.model.ResumeData;
import com.instaworkflow.resumeapi.repository.ResumeDataRepository;

@Service
public class ResumeService {

    private static final Logger log = LoggerFactory.getLogger(ResumeService.class);

    private final List<ResumeRendererStrategy> strategies;
    private final ValidationService validator;

    private final ResumeDataRepository resumeDataRepository;

    public ResumeService(List<ResumeRendererStrategy> strategies, ValidationService validator, ResumeDataRepository resumeDataRepository) {
        this.strategies = strategies;
        this.validator = validator;
        this.resumeDataRepository = resumeDataRepository;
    }

    @Async("pdfGenerationExecutor")
    public CompletableFuture<byte[]> generatePdf(String templateId, JsonNode data, EngineType engine, java.util.Map<String, String> theme) {
        validator.validate(data);

        for (ResumeRendererStrategy strategy : strategies) {
            if (strategy.supports(engine)) {
                return CompletableFuture.completedFuture(strategy.render(templateId, data, theme));
            }
        }
        throw new IllegalArgumentException("No strategy found for engine: " + engine);
    }

    public UUID saveResumeData(Long tenantId, JsonNode payload) {
        validator.validate(payload);
        ResumeData resumeData = new ResumeData();
        resumeData.setTenantId(tenantId);
        resumeData.setJsonPayload(payload);
        return resumeDataRepository.save(resumeData).getId();
    }

    public Optional<ResumeData> getResumeData(UUID id, Long tenantId) {
        return resumeDataRepository.findByIdAndTenantId(id, tenantId);
    }

    public void updateResumeData(UUID id, Long tenantId, JsonNode payload) {
        validator.validate(payload);
        resumeDataRepository.findByIdAndTenantId(id, tenantId).ifPresent(resumeData -> {
            resumeData.setJsonPayload(payload);
            resumeDataRepository.save(resumeData);
        });
    }
}
