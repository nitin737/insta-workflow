package com.instaworkflow.api.controller;

import com.instaworkflow.api.dto.CarouselData;
import com.instaworkflow.api.dto.GenerationRequest;
import com.instaworkflow.api.service.GenerationService;
import com.instaworkflow.api.entity.Job;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class GenerationController {

    private static final Logger logger = LoggerFactory.getLogger(GenerationController.class);

    private final GenerationService generationService;

    public GenerationController(GenerationService generationService) {
        this.generationService = generationService;
    }

    @PostMapping("/carousels")
    public ResponseEntity<CarouselResponse> generate(@Valid @RequestBody GenerationRequest request) {
        Job job = generationService.processGeneration(request);
        return ResponseEntity.ok(new CarouselResponse("rendered", job.getImageUrls(), job.getId().toString()));
    }

    public record CarouselResponse(String status, List<String> imageUrls, String jobId) {
    }

    @GetMapping("/carousels/json")
    public ResponseEntity<CarouselData> getCarouselJSON(
            @RequestParam String topic,
            @RequestParam(defaultValue = "default") String pillar,
            @RequestParam(defaultValue = "default") String tone,
            @RequestParam(required = false) String targetDate) {
        GenerationRequest request = GenerationRequest.builder()
                .topic(topic)
                .pillar(pillar)
                .tone(tone)
                .targetDate(targetDate)
                .build();
        CarouselData data = generationService.generateCarouselJSON(request);
        return ResponseEntity.ok(data);
    }
}
