package com.example.resumeapi.controller;

import com.example.resumeapi.model.GenerateRequest;
import com.example.resumeapi.service.ResumeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/resumes")
public class ResumeController {

    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping("/generate")
    public CompletableFuture<ResponseEntity<byte[]>> generate(@Valid @RequestBody GenerateRequest request) {
        return resumeService.generatePdf(request.templateId(), request.data())
                .thenApply(pdfBytes -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_PDF)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"resume.pdf\"")
                        .body(pdfBytes));
    }
}
