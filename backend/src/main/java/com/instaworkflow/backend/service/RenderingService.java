package com.instaworkflow.backend.service;

import com.instaworkflow.backend.dto.CarouselData;
import com.instaworkflow.backend.service.storage.StorageService;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
public class RenderingService {

    private static final Logger log = LoggerFactory.getLogger(RenderingService.class);

    private static final int VIEWPORT_WIDTH = 1080;
    private static final int VIEWPORT_HEIGHT = 1080;
    private static final String BASE_URL = "http://localhost:8080/";
    private static final String DEFAULT_TEMPLATE = "v1";

    private final TemplateEngine templateEngine;
    private final StorageService storageService;

    public RenderingService(TemplateEngine templateEngine, StorageService storageService) {
        this.templateEngine = templateEngine;
        this.storageService = storageService;
    }

    public List<String> renderCarousel(CarouselData data, String templateName) {
        var runId = UUID.randomUUID().toString().substring(0, 8);
        log.info("Starting carousel rendering with runId: {}", runId);

        var baseTemplate = (templateName != null && !templateName.isBlank()) ? templateName : DEFAULT_TEMPLATE;

        try (var playwright = Playwright.create();
                var executor = Executors.newVirtualThreadPerTaskExecutor()) {

            var launchOptions = new BrowserType.LaunchOptions().setHeadless(true);
            try (var browser = playwright.chromium().launch(launchOptions);
                    var context = browser.newContext(new Browser.NewContextOptions()
                            .setViewportSize(VIEWPORT_WIDTH, VIEWPORT_HEIGHT)
                            .setBaseURL(BASE_URL));
                    var page = context.newPage()) {

                var slides = Arrays.asList(
                        data.slide1(),
                        data.slide2(),
                        data.slide3(),
                        data.slide4(),
                        data.slide5());

                var uploadTasks = new ArrayList<Future<String>>();

                for (int i = 0; i < slides.size(); i++) {
                    int slideNumber = i + 1;
                    var slideData = slides.get(i);

                    if (slideData == null) {
                        log.warn("No data for slide {}, skipping.", slideNumber);
                        continue;
                    }

                    byte[] screenshotBytes = renderSlideToBytes(page, data, slideData, slideNumber, baseTemplate);
                    var safeTopic = data.topic() != null ? data.topic().replaceAll("[^a-zA-Z0-9-_]", "").toLowerCase()
                            : "topic";
                    var fileName = "%s_slide_%d_%s".formatted(safeTopic, slideNumber, runId);

                    // Submit IO-bound storage operations to virtual threads
                    uploadTasks.add(executor.submit(() -> storageService.saveImage(screenshotBytes, fileName)));
                }

                // Gather all completed upload results
                return uploadTasks.stream()
                        .map(this::getFutureResult)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            log.error("Carousel rendering process failed for runId: {}", runId, e);
            throw new RuntimeException("Failed to render carousel: " + runId, e);
        }
    }

    private byte[] renderSlideToBytes(Page page, CarouselData carouselData, Object slideData, int slideNumber,
            String baseTemplate) {
        var slideTemplate = "%s/slide-%d.template".formatted(baseTemplate, slideNumber);

        var context = new Context();
        context.setVariable("carousel", carouselData);
        context.setVariable("slide", slideData);

        var html = templateEngine.process(slideTemplate, context);

        page.navigate(BASE_URL);
        page.setContent(html);
        page.waitForLoadState();

        var slideLocator = page.locator("#slide-" + slideNumber);
        slideLocator.scrollIntoViewIfNeeded();
        return slideLocator.screenshot();
    }

    private String getFutureResult(Future<String> future) {
        try {
            return future.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Image upload task was interrupted", e);
            throw new RuntimeException("Image upload task was interrupted", e);
        } catch (ExecutionException e) {
            log.error("Failed to retrieve saved image url", e);
            throw new RuntimeException("Image upload task failed", e.getCause() != null ? e.getCause() : e);
        }
    }
}
