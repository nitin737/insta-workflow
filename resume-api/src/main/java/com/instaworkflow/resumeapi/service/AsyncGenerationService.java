package com.instaworkflow.resumeapi.service;

import com.instaworkflow.resumeapi.model.AsyncGenerateRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Map;

@Service
public class AsyncGenerationService {

    private static final Logger log = LoggerFactory.getLogger(AsyncGenerationService.class);

    private final ResumeService resumeService;
    private final WebClient webClient;

    public AsyncGenerationService(ResumeService resumeService) {
        this.resumeService = resumeService;
        this.webClient = WebClient.builder().build();
    }

    @Async("pdfGenerationExecutor")
    public void generateAndNotify(AsyncGenerateRequest request) {
        log.info("Starting async generation for template {}", request.templateId());
        
        try {
            resumeService.generatePdf(request.templateId(), request.data(), request.engine(), request.theme())
                .thenAccept(pdfBytes -> {
                    log.info("PDF generated successfully. Sending to webhook: {}", request.webhookUrl());
                    sendToWebhook(request.webhookUrl(), pdfBytes, "success", null);
                })
                .exceptionally(ex -> {
                    log.error("Failed to generate PDF", ex);
                    sendToWebhook(request.webhookUrl(), null, "failed", ex.getMessage());
                    return null;
                });
        } catch (Exception ex) {
            log.error("Failed to start PDF generation", ex);
            sendToWebhook(request.webhookUrl(), null, "failed", ex.getMessage());
        }
    }

    private void sendToWebhook(String webhookUrl, byte[] pdfBytes, String status, String errorMsg) {
        try {
            // Can use multipart/form-data to send the file along with status
            if (pdfBytes != null) {
                ByteArrayResource resource = new ByteArrayResource(pdfBytes) {
                    @Override
                    public String getFilename() {
                        return "resume.pdf";
                    }
                };
                
                MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
                body.add("file", resource);
                body.add("status", status);

                webClient.post()
                        .uri(webhookUrl)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .bodyValue(body)
                        .retrieve()
                        .toBodilessEntity()
                        .subscribe(
                                response -> log.info("Webhook notified successfully"),
                                error -> log.error("Failed to notify webhook", error)
                        );
            } else {
                webClient.post()
                        .uri(webhookUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(Map.of("status", status, "error", errorMsg != null ? errorMsg : "Unknown error"))
                        .retrieve()
                        .toBodilessEntity()
                        .subscribe(
                                response -> log.info("Webhook error notification sent"),
                                error -> log.error("Failed to send webhook error notification", error)
                        );
            }
        } catch (Exception e) {
            log.error("Exception while sending webhook", e);
        }
    }
}
