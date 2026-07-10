package com.instaworkflow.resumeapi.template;

import com.instaworkflow.resumeapi.model.TemplateInfo;
import java.util.List;

public interface TemplateProvider {
    String getTemplateContent(String templateId);
    List<TemplateInfo> listTemplates();
}
