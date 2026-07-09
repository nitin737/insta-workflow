package com.example.resumeapi.template;

import com.example.resumeapi.model.TemplateInfo;
import java.util.List;

public interface TemplateProvider {
    String getTemplateContent(String templateId);
    List<TemplateInfo> listTemplates();
}
