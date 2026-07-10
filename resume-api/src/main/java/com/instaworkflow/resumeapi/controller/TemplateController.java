package com.instaworkflow.resumeapi.controller;

import com.instaworkflow.resumeapi.model.TemplateInfo;
import com.instaworkflow.resumeapi.template.TemplateProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/templates")
public class TemplateController {

    private final TemplateProvider templateProvider;

    public TemplateController(TemplateProvider templateProvider) {
        this.templateProvider = templateProvider;
    }

    @GetMapping
    public List<TemplateInfo> listTemplates() {
        return templateProvider.listTemplates();
    }
}
