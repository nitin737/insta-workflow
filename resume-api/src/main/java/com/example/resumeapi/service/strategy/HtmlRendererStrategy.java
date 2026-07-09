package com.example.resumeapi.service.strategy;

import com.example.resumeapi.compiler.PdfRenderer;
import com.example.resumeapi.model.EngineType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;

@Component
public class HtmlRendererStrategy implements ResumeRendererStrategy {

    private static final Logger log = LoggerFactory.getLogger(HtmlRendererStrategy.class);

    private final SpringTemplateEngine templateEngine;
    private final PdfRenderer pdfRenderer;
    private final ObjectMapper objectMapper;

    public HtmlRendererStrategy(@Qualifier("resumeTemplateEngine") SpringTemplateEngine templateEngine, 
                                PdfRenderer pdfRenderer,
                                ObjectMapper objectMapper) {
        this.templateEngine = templateEngine;
        this.pdfRenderer = pdfRenderer;
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean supports(EngineType engineType) {
        return EngineType.HTML == engineType;
    }

    @Override
    public byte[] render(String templateId, JsonNode data) {
        // Flatten JSON into a Thymeleaf context
        Map<String, Object> variables = objectMapper.convertValue(data, new TypeReference<>() {});
        Context ctx = new Context();
        ctx.setVariables(variables);

        // Thymeleaf resolves: classpath:templates/<templateId>/template.html
        String templatePath = templateId + "/template";
        log.debug("Rendering HTML template '{}'", templatePath);
        String xhtml = templateEngine.process(templatePath, ctx);

        return pdfRenderer.render(xhtml);
    }
}
