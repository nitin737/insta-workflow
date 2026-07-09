package com.example.resumeapi.service;

import com.example.resumeapi.compiler.PdfRenderer;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class ResumeService {

    private static final Logger log = LoggerFactory.getLogger(ResumeService.class);

    private final SpringTemplateEngine templateEngine;
    private final PdfRenderer pdfRenderer;
    private final ValidationService validator;
    private final ObjectMapper objectMapper;

    public ResumeService(@Qualifier("resumeTemplateEngine") SpringTemplateEngine templateEngine, PdfRenderer pdfRenderer,
                         ValidationService validator, ObjectMapper objectMapper) {
        this.templateEngine = templateEngine;
        this.pdfRenderer = pdfRenderer;
        this.validator = validator;
        this.objectMapper = objectMapper;
    }

    @Async("pdfGenerationExecutor")
    public CompletableFuture<byte[]> generatePdf(String templateId, JsonNode data) {
        validator.validate(data);

        // Flatten JSON into a Thymeleaf context
        Map<String, Object> variables = objectMapper.convertValue(data, new TypeReference<>() {});
        Context ctx = new Context();
        ctx.setVariables(variables);

        // Thymeleaf resolves: classpath:templates/<templateId>/template.html
        String templatePath = templateId + "/template";
        log.debug("Rendering template '{}'", templatePath);
        String xhtml = templateEngine.process(templatePath, ctx);

        return CompletableFuture.completedFuture(pdfRenderer.render(xhtml));
    }
}
