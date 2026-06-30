package com.instaworkflow.backend.controller;

import com.instaworkflow.backend.dto.CarouselData;
import com.instaworkflow.backend.dto.GenerationRequest;
import com.instaworkflow.backend.service.GenerationService;
import com.instaworkflow.backend.service.RenderingService;
import com.instaworkflow.backend.entity.Job;
import com.instaworkflow.backend.entity.JobStatus;
import com.instaworkflow.backend.repository.JobRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class GenerationController {

    private final GenerationService generationService;
    private final RenderingService renderingService;
    private final JobRepository jobRepository;

    public GenerationController(GenerationService generationService, RenderingService renderingService, JobRepository jobRepository) {
        this.generationService = generationService;
        this.renderingService = renderingService;
        this.jobRepository = jobRepository;
    }

    @PostMapping("/generate")
    public ResponseEntity<CarouselResponse> generate(@Valid @RequestBody GenerationRequest request) {
        Job job = new Job(request.topic(), JobStatus.PENDING);
        job = jobRepository.save(job);

        try {
            job.setStatus(JobStatus.GENERATING);
            jobRepository.save(job);
            CarouselData data = generationService.generateCarouselJSON(request);

            job.setStatus(JobStatus.RENDERING);
            jobRepository.save(job);
            List<String> imageUrls = renderingService.renderCarousel(data);

            job.setStatus(JobStatus.COMPLETED);
            job.setImageUrls(imageUrls);
            jobRepository.save(job);

            return ResponseEntity.ok(new CarouselResponse("rendered", imageUrls, job.getId().toString()));
        } catch (Exception e) {
            job.setStatus(JobStatus.FAILED);
            jobRepository.save(job);
            throw e;
        }
    }

    public record CarouselResponse(String status, List<String> imageUrls, String jobId) {}
}
