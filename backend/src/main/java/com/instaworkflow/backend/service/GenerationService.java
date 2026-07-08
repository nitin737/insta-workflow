package com.instaworkflow.backend.service;

import com.instaworkflow.backend.dto.CarouselData;
import com.instaworkflow.backend.dto.GenerationRequest;
import com.instaworkflow.backend.entity.Job;
import com.instaworkflow.backend.entity.JobStatus;
import com.instaworkflow.backend.repository.JobRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class GenerationService {

    private static final Logger log = LoggerFactory.getLogger(GenerationService.class);

    @Value("classpath:prompts/carousel-generation-prompt.st")
    private Resource promptResource;

    private final ChatModel chatModel;
    private final RenderingService renderingService;
    private final JobRepository jobRepository;

    public GenerationService(ChatModel chatModel, RenderingService renderingService, JobRepository jobRepository) {
        this.chatModel = chatModel;
        this.renderingService = renderingService;
        this.jobRepository = jobRepository;
    }

    public Job processGeneration(final GenerationRequest request) {
        var job = new Job(request.topic(), JobStatus.PENDING);
        job = jobRepository.save(job);
        log.info("Created new job with ID: {} for topic: {}", job.getId(), job.getTopic());

        try {
            updateJobStatus(job, JobStatus.GENERATING);
            var data = generateCarouselJSON(request);

            updateJobStatus(job, JobStatus.RENDERING);
            var imageUrls = renderingService.renderCarousel(data, request.templateName());

            job.setStatus(JobStatus.COMPLETED);
            job.setImageUrls(imageUrls);
            return jobRepository.save(job);

        } catch (Exception e) {
            log.error("Error processing generation request for job ID: {}", job.getId(), e);
            updateJobStatus(job, JobStatus.FAILED);
            throw new RuntimeException("Failed to process generation request", e);
        }
    }

    public CarouselData generateCarouselJSON(final GenerationRequest request) {
        var outputConverter = new BeanOutputConverter<>(CarouselData.class);
        var format = outputConverter.getFormat();
        var topic = request.topic();

        log.info("Starting JSON generation for topic: {}", topic);

        var promptTemplate = new PromptTemplate(promptResource);
        var prompt = promptTemplate.create(Map.of("topic", topic, "format", format));

        log.debug("Calling chat model for topic");
        var response = chatModel.call(prompt).getResult().getOutput().getText();

        log.debug("Successfully received response from chat model, converting to CarouselData");
        return outputConverter.convert(response);
    }

    private void updateJobStatus(Job job, JobStatus status) {
        job.setStatus(status);
        jobRepository.save(job);
        log.debug("Updated job {} status to {}", job.getId(), status);
    }
}
