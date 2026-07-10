package com.instaworkflow.resumeapi.service.strategy;

import com.instaworkflow.resumeapi.compiler.LatexCompiler;
import com.instaworkflow.resumeapi.model.EngineType;
import com.instaworkflow.resumeapi.template.LatexUtils;
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
public class LatexRendererStrategy implements ResumeRendererStrategy {

    private static final Logger log = LoggerFactory.getLogger(LatexRendererStrategy.class);

    private final SpringTemplateEngine textTemplateEngine;
    private final LatexCompiler latexCompiler;
    private final ObjectMapper objectMapper;
    private final LatexUtils latexUtils;

    public LatexRendererStrategy(@Qualifier("resumeTextTemplateEngine") SpringTemplateEngine textTemplateEngine,
                                 LatexCompiler latexCompiler,
                                 ObjectMapper objectMapper,
                                 LatexUtils latexUtils) {
        this.textTemplateEngine = textTemplateEngine;
        this.latexCompiler = latexCompiler;
        this.objectMapper = objectMapper;
        this.latexUtils = latexUtils;
    }

    @Override
    public boolean supports(EngineType engineType) {
        return EngineType.LATEX == engineType;
    }

    @Override
    public byte[] render(String templateId, JsonNode data, java.util.Map<String, String> theme) {
        // Flatten JSON into a Thymeleaf context
        Map<String, Object> variables = objectMapper.convertValue(data, new TypeReference<>() {});
        variables.put("latexUtils", latexUtils);
        Context ctx = new Context();
        ctx.setVariables(variables);
        ctx.setVariable("theme", theme != null ? theme : java.util.Collections.emptyMap());

        // Thymeleaf resolves: classpath:templates/<templateId>/template.tex
        String templatePath = templateId + "/template";
        log.debug("Rendering LaTeX template '{}'", templatePath);
        String latexSource = textTemplateEngine.process(templatePath, ctx);

        return latexCompiler.compile(latexSource);
    }
}
