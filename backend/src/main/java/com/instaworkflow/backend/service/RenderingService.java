package com.instaworkflow.backend.service;

import com.instaworkflow.backend.dto.CarouselData;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class RenderingService {

    private final TemplateEngine templateEngine;

    public RenderingService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public List<String> renderCarousel(CarouselData data) {
        Context context = new Context();
        context.setVariable("carousel", data);
        String html = templateEngine.process("carousel-template", context);

        List<String> outputFiles = new ArrayList<>();
        String runId = UUID.randomUUID().toString();

        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
            // Viewport must match the slide size, Playwright defaults to 1280x720, we need 1080x1080
            Page page = browser.newPage(new Browser.NewPageOptions().setViewportSize(1080, 1080));
            
            page.setContent(html);
            page.waitForLoadState();
            
            int numSlides = data.slides().size();
            for (int i = 0; i < numSlides; i++) {
                String fileName = "slide_" + runId + "_" + (i + 1) + ".png";
                
                page.evaluate("window.scrollTo(" + (i * 1080) + ", 0)");
                page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(fileName)));
                
                outputFiles.add(fileName);
            }
        }
        
        return outputFiles;
    }
}
