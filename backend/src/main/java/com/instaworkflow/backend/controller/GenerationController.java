package com.instaworkflow.backend.controller;

import com.instaworkflow.backend.dto.CarouselData;
import com.instaworkflow.backend.dto.GenerationRequest;
import com.instaworkflow.backend.service.GenerationService;
import com.instaworkflow.backend.service.RenderingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class GenerationController {

    private final GenerationService generationService;
    private final RenderingService renderingService;

    public GenerationController(GenerationService generationService, RenderingService renderingService) {
        this.generationService = generationService;
        this.renderingService = renderingService;
    }

    @PostMapping("/generate")
    public ResponseEntity<CarouselResponse> generate(@Valid @RequestBody GenerationRequest request) {
        CarouselData data = generationService.generateCarousel(request);
        List<String> imageUrls = renderingService.renderCarousel(data);
        return ResponseEntity.ok(new CarouselResponse("rendered", imageUrls));
    }

    public record CarouselResponse(String status, List<String> imageUrls) {}
}
