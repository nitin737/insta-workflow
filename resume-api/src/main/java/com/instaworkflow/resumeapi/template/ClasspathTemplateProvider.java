package com.instaworkflow.resumeapi.template;

import com.instaworkflow.resumeapi.exception.TemplateNotFoundException;
import com.instaworkflow.resumeapi.model.TemplateInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class ClasspathTemplateProvider implements TemplateProvider {

    @Value("${resume.template.base-path:classpath:templates/}")
    private String basePath;

    private final ObjectMapper objectMapper;

    public ClasspathTemplateProvider(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String getTemplateContent(String templateId) {
        try {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource resource = resolver.getResource(basePath + templateId + "/template.tex");
            if (!resource.exists()) {
                throw new TemplateNotFoundException("Template not found: " + templateId);
            }
            return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new TemplateNotFoundException("Error reading template: " + templateId);
        }
    }

    @Override
    public List<TemplateInfo> listTemplates() {
        List<TemplateInfo> templates = new ArrayList<>();
        try {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources(basePath + "*/metadata.json");
            for (Resource resource : resources) {
                TemplateInfo info = objectMapper.readValue(resource.getInputStream(), TemplateInfo.class);
                templates.add(info);
            }
        } catch (IOException e) {
            // Log warning
        }
        return templates;
    }
}
