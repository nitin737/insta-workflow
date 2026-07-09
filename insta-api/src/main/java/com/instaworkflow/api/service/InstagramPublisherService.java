package com.instaworkflow.api.service;

import com.instaworkflow.api.config.InstagramProperties;
import com.instaworkflow.api.entity.Job;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class InstagramPublisherService {

    private static final Logger log = LoggerFactory.getLogger(InstagramPublisherService.class);
    private final InstagramProperties properties;
    private final RestClient restClient;

    public InstagramPublisherService(InstagramProperties properties) {
        this.properties = properties;
        this.restClient = RestClient.builder()
                .baseUrl(properties.getApiUrl() != null ? properties.getApiUrl() : "https://graph.facebook.com/v19.0")
                .build();
    }

    /**
     * Publishes a carousel job to Instagram.
     * Uses Resilience4j @Retry for rate limits.
     */
    @Retry(name = "instagram")
    public void publishCarousel(Job job) {
        log.info("Starting publish process for job ID: {}", job.getId());
        
        if (job.getImageUrls() == null || job.getImageUrls().isEmpty()) {
            throw new IllegalArgumentException("No image URLs found for job " + job.getId());
        }

        String accountId = properties.getAccount().getId();
        String accessToken = properties.getAccess().getToken();

        if (accountId == null || accountId.isBlank() || accessToken == null || accessToken.isBlank()) {
            log.warn("Instagram credentials not configured. Skipping publish for job ID: {}", job.getId());
            throw new IllegalStateException("Instagram credentials are not configured");
        }

        // 1. Create item containers for each image
        List<String> containerIds = new ArrayList<>();
        for (String url : job.getImageUrls()) {
            String containerId = createItemContainer(accountId, accessToken, url);
            containerIds.add(containerId);
        }

        // 2. Create carousel container
        String caption = job.getTopic() != null ? job.getTopic() : "Generated automatically by InstaWorkflow";
        String carouselId = createCarouselContainer(accountId, accessToken, containerIds, caption);

        // 3. Publish the carousel
        publishContainer(accountId, accessToken, carouselId);
        
        log.info("Successfully published carousel for job ID: {}. Carousel ID: {}", job.getId(), carouselId);
    }

    private String createItemContainer(String accountId, String accessToken, String imageUrl) {
        log.debug("Creating item container for URL: {}", imageUrl);
        var response = restClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/{accountId}/media")
                        .queryParam("image_url", imageUrl)
                        .queryParam("is_carousel_item", "true")
                        .queryParam("access_token", accessToken)
                        .build(accountId))
                .retrieve()
                .body(Map.class);

        if (response != null && response.containsKey("id")) {
            return response.get("id").toString();
        }
        throw new RuntimeException("Failed to create item container: " + response);
    }

    private String createCarouselContainer(String accountId, String accessToken, List<String> childrenIds, String caption) {
        log.debug("Creating carousel container with {} children", childrenIds.size());
        String childrenStr = String.join(",", childrenIds);

        var response = restClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/{accountId}/media")
                        .queryParam("media_type", "CAROUSEL")
                        .queryParam("children", childrenStr)
                        .queryParam("caption", caption)
                        .queryParam("access_token", accessToken)
                        .build(accountId))
                .retrieve()
                .body(Map.class);

        if (response != null && response.containsKey("id")) {
            return response.get("id").toString();
        }
        throw new RuntimeException("Failed to create carousel container: " + response);
    }

    private void publishContainer(String accountId, String accessToken, String creationId) {
        log.debug("Publishing container ID: {}", creationId);
        var response = restClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/{accountId}/media_publish")
                        .queryParam("creation_id", creationId)
                        .queryParam("access_token", accessToken)
                        .build(accountId))
                .retrieve()
                .body(Map.class);

        if (response == null || !response.containsKey("id")) {
            throw new RuntimeException("Failed to publish container: " + response);
        }
    }
}
