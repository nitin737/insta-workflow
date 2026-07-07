package com.instaworkflow.backend.service;

import com.instaworkflow.backend.dto.CarouselData;
import com.instaworkflow.backend.dto.GenerationRequest;
import com.instaworkflow.backend.entity.Job;
import com.instaworkflow.backend.entity.JobStatus;
import com.instaworkflow.backend.repository.JobRepository;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Service
public class GenerationService {

    private static final Logger logger = LoggerFactory.getLogger(GenerationService.class);

    private final ChatModel chatModel;
    private final RenderingService renderingService;
    private final JobRepository jobRepository;

    @Value("classpath:gemini-carousel-prompt-1.md")
    private Resource promptResource;

    public GenerationService(ChatModel chatModel, RenderingService renderingService, JobRepository jobRepository) {
        this.chatModel = chatModel;
        this.renderingService = renderingService;
        this.jobRepository = jobRepository;
    }

    public Job processGeneration(GenerationRequest request) {
        Job job = new Job(request.topic(), JobStatus.PENDING);
        job = jobRepository.save(job);
        logger.info("Created new job with ID: {} for topic: {}", job.getId(), job.getTopic());

        try {
            job.setStatus(JobStatus.GENERATING);
            jobRepository.save(job);
            CarouselData data = generateCarouselJSON(request);

            job.setStatus(JobStatus.RENDERING);
            jobRepository.save(job);
            List<String> imageUrls = renderingService.renderCarousel(data, request.templateName());

            job.setStatus(JobStatus.COMPLETED);
            job.setImageUrls(imageUrls);
            job = jobRepository.save(job);

            return job;
        } catch (Exception e) {
            logger.error("Error processing generation request", e);
            job.setStatus(JobStatus.FAILED);
            jobRepository.save(job);
            throw e;
        }
    }

    public CarouselData generateCarouselJSON(GenerationRequest request) {

        BeanOutputConverter<CarouselData> outputConverter = new BeanOutputConverter<>(CarouselData.class);
        String format = outputConverter.getFormat();
        String topic = request.topic() != null ? request.topic() : "https://github.com/python-pillow/Pillow";

        String template = """
                            You are a technical content creator specializing in writing high-retention Instagram Carousel posts for Go (Golang) developers focusing on CLI tooling and developer infrastructure.
                            Your task is to generate a structured JSON payload for a 5-slide carousel about a specific Go CLI library or tool. You must strictly adhere to the JSON schema below and output ONLY valid JSON without any markdown code block wrappers (do not include ```json ... ```) or conversational preamble.

                            Here is the topic/library to write about:{topic}

                            ### Content Guidelines for each Slide:

                            - Slide 1 (GitHub Repo Card): Write the GitHub metadata for the repository including owner, repo, star count, big repository name, description highlighting Go/golang, total contributors count, language percentages, watchers, forks, latest release, tags, license, and about text.
                            - Slide 2 (Pain & Cure): Define the headline. Identify a common "pain" developers face without this tool. Provide a concise "cure" explaining how this tool solves it. List exactly 3 key "features" highlighting its benefits.
                            - Slide 3 (Before & After): Write a catchy headline. Provide "beforeCode" demonstrating the heavy or manual way of doing things, and "afterCode" showing the elegant solution using the library. Include a "takeaway" sentence summarizing the impact. Use \\n for newlines in code strings.
                            - Slide 4 (Features Deep Dive): Provide a headline and an array of 4 "points", each containing a short "title" and "desc" explaining specific technical advantages or design philosophies of the library.
                            - Slide 5 (Quickstart & CTAs): Write a closing headline. Provide a one-liner "quickstart" terminal command. Provide a "minimalSetup" Go code snippet showing integration (use \\n for line breaks). Include 2 "resources" links (website/docs and GitHub). End with 4 "ctas" (call-to-actions) with emoji icons (e.g., save, comment, star, share).

                            ### JSON Schema Output Format:
                {format}
                """;

        PromptTemplate promptTemplate = new PromptTemplate(template);
        Prompt prompt = promptTemplate.create(Map.of("topic", topic, "format", format));
        logger.info("Generated Prompt: \n{}", prompt.getContents());
        String response = chatModel.call(prompt).getResult().getOutput().getText();
        logger.info("Model Response: \n{}", response);
        return outputConverter.convert(response);
    }
}
