package com.instaworkflow.resumeapi.controller;

import com.instaworkflow.resumeapi.model.GenerateRequest;
import com.instaworkflow.resumeapi.service.ResumeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/resumes")
public class ResumeController {

    private final ResumeService resumeService;
    private final com.instaworkflow.resumeapi.service.AsyncGenerationService asyncGenerationService;

    public ResumeController(ResumeService resumeService, com.instaworkflow.resumeapi.service.AsyncGenerationService asyncGenerationService) {
        this.resumeService = resumeService;
        this.asyncGenerationService = asyncGenerationService;
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<byte[]>> generate(@Valid @RequestBody GenerateRequest request) {
        return resumeService.generatePdf(request.templateId(), request.data(), request.engine(), request.theme())
                .thenApply(pdfBytes -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_PDF)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"resume.pdf\"")
                        .body(pdfBytes));
    }

    @PostMapping("/data")
    public ResponseEntity<java.util.Map<String, java.util.UUID>> saveResumeData(
            @RequestBody com.fasterxml.jackson.databind.JsonNode payload,
            org.springframework.security.core.Authentication authentication) {
        com.instaworkflow.resumeapi.model.Tenant tenant = (com.instaworkflow.resumeapi.model.Tenant) authentication.getPrincipal();
        java.util.UUID id = resumeService.saveResumeData(tenant.getId(), payload);
        return ResponseEntity.ok(java.util.Map.of("id", id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<com.fasterxml.jackson.databind.JsonNode> getResumeData(
            @PathVariable java.util.UUID id,
            org.springframework.security.core.Authentication authentication) {
        com.instaworkflow.resumeapi.model.Tenant tenant = (com.instaworkflow.resumeapi.model.Tenant) authentication.getPrincipal();
        return resumeService.getResumeData(id, tenant.getId())
                .map(resumeData -> ResponseEntity.ok(resumeData.getJsonPayload()))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateResumeData(
            @PathVariable java.util.UUID id,
            @RequestBody com.fasterxml.jackson.databind.JsonNode payload,
            org.springframework.security.core.Authentication authentication) {
        com.instaworkflow.resumeapi.model.Tenant tenant = (com.instaworkflow.resumeapi.model.Tenant) authentication.getPrincipal();
        resumeService.updateResumeData(id, tenant.getId(), payload);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/generate")
    public CompletableFuture<ResponseEntity<byte[]>> generateFromSaved(
            @PathVariable java.util.UUID id,
            @RequestParam String templateId,
            @RequestParam(required = false) com.instaworkflow.resumeapi.model.EngineType engine,
            org.springframework.security.core.Authentication authentication) {
        com.instaworkflow.resumeapi.model.Tenant tenant = (com.instaworkflow.resumeapi.model.Tenant) authentication.getPrincipal();
        return resumeService.getResumeData(id, tenant.getId())
                .map(resumeData -> resumeService.generatePdf(templateId, resumeData.getJsonPayload(), engine, java.util.Collections.emptyMap())
                        .thenApply(pdfBytes -> ResponseEntity.ok()
                                .contentType(MediaType.APPLICATION_PDF)
                                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"resume.pdf\"")
                                .body(pdfBytes)))
                .orElse(CompletableFuture.completedFuture(ResponseEntity.notFound().build()));
    }

    @PostMapping("/generate-async")
    public ResponseEntity<Void> generateAsync(
            @Valid @RequestBody com.instaworkflow.resumeapi.model.AsyncGenerateRequest request,
            org.springframework.security.core.Authentication authentication) {
        // Authenticate tenant (optional depending on if async needs tracking, but good for rate limit)
        com.instaworkflow.resumeapi.model.Tenant tenant = (com.instaworkflow.resumeapi.model.Tenant) authentication.getPrincipal();
        // The generation happens asynchronously
        asyncGenerationService.generateAndNotify(request);
        return ResponseEntity.accepted().build();
    }

    @GetMapping(value = "/schema", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource> getSchema() {
        Resource resource = new ClassPathResource("resume-schema.json");
        if (resource.exists()) {
            return ResponseEntity.ok().body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
