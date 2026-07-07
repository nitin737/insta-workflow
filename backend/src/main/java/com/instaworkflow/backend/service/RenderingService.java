package com.instaworkflow.backend.service;

import com.instaworkflow.backend.dto.CarouselData;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.instaworkflow.backend.service.storage.StorageService;

@Service
public class RenderingService {

    private final TemplateEngine templateEngine;
    private final StorageService storageService;

    public RenderingService(TemplateEngine templateEngine, StorageService storageService) {
        this.templateEngine = templateEngine;
        this.storageService = storageService;
    }

    public List<String> renderCarousel(CarouselData data, String templateName) {
        Context context = new Context();
        context.setVariable("carousel", data);

        List<String> outputFiles = new ArrayList<>();
        String runId = UUID.randomUUID().toString();

        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
            // Viewport must match the slide size, Playwright defaults to 1280x720, we need
            // 1080x1080
            Page page = browser.newPage(new Browser.NewPageOptions()
                    .setViewportSize(1080, 1080)
                    .setBaseURL("http://localhost:8080/"));

            int numSlides = 5;
            for (int i = 0; i < numSlides; i++) {
                String slideTemplate = "slide-" + (i + 1);
                String html = templateEngine.process(slideTemplate, context);
                
                page.setContent(html);
                page.waitForLoadState();

                String fileName = "slide_" + runId + "_" + (i + 1);

                com.microsoft.playwright.Locator slideLocator = page.locator("#slide-" + (i + 1));
                slideLocator.scrollIntoViewIfNeeded();
                byte[] screenshotBytes = slideLocator.screenshot();

                try {
                    String urlOrPath = storageService.saveImage(screenshotBytes, fileName);
                    outputFiles.add(urlOrPath);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to save or upload slide: " + fileName, e);
                }
            }
        }

        return outputFiles;
    }
}
